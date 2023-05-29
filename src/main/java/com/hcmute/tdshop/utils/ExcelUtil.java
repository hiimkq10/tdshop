package com.hcmute.tdshop.utils;

import com.hcmute.tdshop.entity.District;
import com.hcmute.tdshop.entity.Province;
import com.hcmute.tdshop.entity.Wards;
import com.hcmute.tdshop.model.AdministrativeArea;
import com.hcmute.tdshop.repository.DistrictRepository;
import com.hcmute.tdshop.repository.ProvinceRepository;
import com.hcmute.tdshop.repository.WardsRepository;
import com.hcmute.tdshop.utils.annotations.ExcelColumnIndex;
import com.hcmute.tdshop.utils.constants.ApplicationConstants;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.transaction.Transactional;
import org.apache.logging.log4j.util.Strings;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class ExcelUtil {
  @Autowired
  ProvinceRepository provinceRepository;

  @Autowired
  DistrictRepository districtRepository;

  @Autowired
  WardsRepository wardsRepository;

  public Workbook getWorkbook(InputStream inputStream) throws IOException {
    return new XSSFWorkbook(inputStream);
  }

  public <T> List<T> getListObjectFromExcelFile(Class<T> objectClass, String sheetName, MultipartFile listObjectFile)
      throws IOException, NoSuchFieldException, InvocationTargetException, NoSuchMethodException,
      InstantiationException, IllegalAccessException, ParseException {
    Workbook workbook = getWorkbook(listObjectFile.getInputStream());
    return GetDataFromSheetAndMapToList(objectClass, sheetName, workbook);
  }

  // Temporary use, waiting to be replaced by flyway
  @Transactional
  public boolean insertDataToDatabase() throws IOException, NoSuchFieldException, InvocationTargetException,
      NoSuchMethodException, InstantiationException, IllegalAccessException, ParseException {
    if (provinceRepository.count() > 0) {
      throw new RuntimeException("Area initilized");
    }
    InputStream inputStream = ExcelUtil.class.getResourceAsStream(ApplicationConstants.AREAS_FILE);
    Workbook workbook = getWorkbook(inputStream);
    System.out.println("Initilize areas");
    List<AdministrativeArea> areas =
        GetDataFromSheetAndMapToList(AdministrativeArea.class, ApplicationConstants.AREAS_SHEET_NAME, workbook);
    int tempSize = areas.size();
    Map<Long, Province> provinceMap = new HashMap<>();
    Map<Long, District> districtMap = new HashMap<>();
    List<Wards> wards = new ArrayList<>();
    AdministrativeArea area;
    for (int i = 0; i < tempSize; i++) {
      area = areas.get(i);
      if (area.getProvinceId() == 0) {
        continue;
      }
      if (!provinceMap.containsKey(area.getProvinceId())) {
        provinceMap.put(area.getProvinceId(), new Province(area.getProvinceId(), area.getProvinceName(), "", null));
      }

      if (area.getDistrictId() == 0) {
        continue;
      }
      if (!districtMap.containsKey(area.getDistrictId())) {
        districtMap.put(area.getDistrictId(), new District(area.getDistrictId(), area.getDistrictName(), "", provinceMap.get(area.getProvinceId()), null));
      }

      if (area.getWardId() == 0) {
        continue;
      }
      wards.add(new Wards(area.getWardId(), area.getWardName(), area.getWardType(), districtMap.get(area.getDistrictId())));
    }
    provinceRepository.saveAll(provinceMap.values());
    districtRepository.saveAll(districtMap.values());
    wardsRepository.saveAll(wards);
    return true;
  }

  // Read data from Excel Sheet (row by row)
  // For each row, create a new object by objectClass
  // If a field in object does not have annotation @ExcelColumnIndex, skip that field
  // Otherwise, get cell from row by using the value of @ExcelColumnIndex, then assign cell value to
  // that field
  // Add that new object to a List<Object> and return list<Object>
  public <T> List<T> GetDataFromSheetAndMapToList(Class<T> objectClass, String sheetName, Workbook workbook)
      throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException,
      NoSuchFieldException, ParseException {
    Sheet sheet = workbook.getSheet(sheetName);
    List<T> listOfObjects = new ArrayList<>();
    T object;
    DataFormatter dataFormatter = new DataFormatter();
    DateTimeFormatter dateTimeFormatter =
        new DateTimeFormatterBuilder().appendPattern("yyyy-MM-dd HH:mm:ss").toFormatter();
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    String value;
    Field[] objectFields = objectClass.getDeclaredFields();
    for (Row nextRow : sheet) {
      if (nextRow.getRowNum() == 0) {
        continue;
      }
      object = objectClass.getConstructor().newInstance();
      for (Field field : objectFields) {
        Field objectField = object.getClass().getDeclaredField(field.getName());
        if (objectField.getAnnotation(ExcelColumnIndex.class) == null) {
          continue;
        }
        objectField.setAccessible(true); // disable field access modifier
        Cell cell = nextRow.getCell(objectField.getAnnotation(ExcelColumnIndex.class).index());
        value = dataFormatter.formatCellValue(cell);
        Class<?> objectFieldType = objectField.getType();
        if (objectFieldType.equals(Long.class) || objectFieldType.equals(Long.TYPE)) {
          if (Strings.isBlank(value)) {
            value = "0";
          }
          objectField.set(object, Long.valueOf(value));
        } else if (objectFieldType.equals(Double.class) || objectFieldType.equals(Double.TYPE)) {
          if (Strings.isBlank(value)) {
            value = "0";
          }
          objectField.set(object, Double.valueOf(value));
        } else if (objectFieldType.equals(Integer.class) || objectFieldType.equals(Integer.TYPE)) {
          if (Strings.isBlank(value)) {
            value = "0";
          }
          objectField.set(object, Integer.valueOf(value));
        } else if (objectFieldType.equals(String.class)) {
          objectField.set(object, value);
        } else if (objectFieldType.equals(LocalDateTime.class)) {
          objectField.set(object, LocalDateTime.parse(value, dateTimeFormatter));
        } else if (objectFieldType.equals(Date.class)) {
          // java.util.Date
          objectField.set(object, simpleDateFormat.parse(value));
        }
      }
      listOfObjects.add(object);
    }
    return listOfObjects;
  }
}
