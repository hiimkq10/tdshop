package com.hcmute.tdshop.utils;

import com.hcmute.tdshop.entity.Attribute;
import com.hcmute.tdshop.entity.Brand;
import com.hcmute.tdshop.entity.Category;
import com.hcmute.tdshop.entity.District;
import com.hcmute.tdshop.entity.Image;
import com.hcmute.tdshop.entity.Product;
import com.hcmute.tdshop.entity.ProductAttribute;
import com.hcmute.tdshop.entity.ProductStatus;
import com.hcmute.tdshop.entity.Province;
import com.hcmute.tdshop.entity.VariationOption;
import com.hcmute.tdshop.entity.Wards;
import com.hcmute.tdshop.enums.AdministrativeTypeEnum;
import com.hcmute.tdshop.enums.ProductStatusEnum;
import com.hcmute.tdshop.model.AdministrativeArea;
import com.hcmute.tdshop.model.ProductExcel;
import com.hcmute.tdshop.repository.AttributeRepository;
import com.hcmute.tdshop.repository.BrandRepository;
import com.hcmute.tdshop.repository.CategoryRepository;
import com.hcmute.tdshop.repository.DistrictRepository;
import com.hcmute.tdshop.repository.ProductRepository;
import com.hcmute.tdshop.repository.ProductStatusRepository;
import com.hcmute.tdshop.repository.ProvinceRepository;
import com.hcmute.tdshop.repository.VariationOptionRepository;
import com.hcmute.tdshop.repository.VariationRepository;
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
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import javax.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
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

  @Autowired
  ProductStatusRepository productStatusRepository;

  @Autowired
  CategoryRepository categoryRepository;

  @Autowired
  BrandRepository brandRepository;

  @Autowired
  AttributeRepository attributeRepository;

  @Autowired
  VariationOptionRepository variationOptionRepository;

  @Autowired
  ProductRepository productRepository;

  public Workbook getWorkbook(InputStream inputStream) throws IOException {
    return new XSSFWorkbook(inputStream);
  }

  public <T> List<T> getListObjectFromExcelFile(Class<T> objectClass, String sheetName, MultipartFile listObjectFile)
      throws IOException, NoSuchFieldException, InvocationTargetException, NoSuchMethodException,
      InstantiationException, IllegalAccessException, ParseException {
    Workbook workbook = getWorkbook(listObjectFile.getInputStream());
    return GetDataFromSheetAndMapToList(objectClass, sheetName, workbook);
  }

  @Transactional
  public boolean insertProductToDatabase() throws IOException, NoSuchFieldException, InvocationTargetException,
      NoSuchMethodException, InstantiationException, IllegalAccessException, ParseException {
    InputStream inputStream = ExcelUtil.class.getResourceAsStream(ApplicationConstants.PRODUCTS_FILE);
    Workbook workbook = getWorkbook(inputStream);
    List<ProductExcel> productExcels =
        GetDataFromSheetAndMapToList(ProductExcel.class, ApplicationConstants.PRODUCTS_SHEET_NAME, workbook);
    ProductStatus productStatus = productStatusRepository.findById(ProductStatusEnum.ONSALE.getId()).get();
    int size = productExcels.size();
    LocalDateTime now = LocalDateTime.now();
    ProductExcel productExcel = null;
    Product product = null;
    List<Category> categories = categoryRepository.findByParentIsNotNull();
    Map<Long, Category> categoryMap = new HashMap<>();
    for (Category category : categories) {
      categoryMap.put(category.getId(), category);
    }
    List<Brand> brands = brandRepository.findAll();
    Map<Long, Brand> brandMap = new HashMap<>();
    for (Brand brand : brands) {
      brandMap.put(brand.getId(), brand);
    }
    List<Attribute> attributes = attributeRepository.findAll();
    Map<Long, Attribute> attributeMap = new HashMap<>();
    for (Attribute attribute : attributes) {
      attributeMap.put(attribute.getId(), attribute);
    }
    List<VariationOption> variationOptions = variationOptionRepository.findAll();
    Map<Long, VariationOption> variationOptionMap = new HashMap<>();
    for (VariationOption variationOption : variationOptions) {
      variationOptionMap.put(variationOption.getId(), variationOption);
    }
    List<Product> products = new ArrayList<>();

    for (int i = 0; i < size; i++) {
      productExcel = productExcels.get(i);

      product = new Product();
      product.setSku(productExcel.getSku());
      product.setName(productExcel.getName());
      product.setImageUrl(productExcel.getImageUrl());
      product.setPrice(productExcel.getPrice());
      product.setDescription(productExcel.getDescription());
      product.setShortDescription(productExcel.getShortDescription());
      product.setTotal(productExcel.getTotal());
      product.setSelAmount(0);
      product.setLength(productExcel.getLength());
      product.setWidth(productExcel.getWidth());
      product.setHeight(productExcel.getHeight());
      product.setWeight(productExcel.getWeight());
      product.setCreatedAt(now);
      product.setStatus(productStatus);
      product.setSetOfCategories(new HashSet<>());
      for (String cateId : productExcel.getCategories().split(", ")) {
        product.getSetOfCategories().add(categoryMap.get(Long.valueOf(cateId)));
        categoryMap.get(Long.valueOf(cateId)).getSetOfProducts().add(product);
      }
      product.setBrand(brandMap.get(productExcel.getBrandId()));
      product.setSetOfImages(new HashSet<>());
      for (String url : productExcel.getImageUrls().split(", ")) {
        product.getSetOfImages().add(new Image(null, url, "", product));
      }

      Long id = product.getSetOfCategories().stream().findFirst().get().getMasterCategory().getId();
      product.setSetOfProductAttributes(new HashSet<>());
      if (id == 1) {
        product.getSetOfProductAttributes().add(new ProductAttribute(null, product.getBrand().getName(), attributeMap.get(1), product));
        product.getSetOfProductAttributes().add(new ProductAttribute(null, "12 tháng", attributeMap.get(2), product));
      } else if (id == 2) {
        product.getSetOfProductAttributes().add(new ProductAttribute(null, product.getBrand().getName(), attributeMap.get(47), product));
        product.getSetOfProductAttributes().add(new ProductAttribute(null, "12 tháng", attributeMap.get(48), product));
      } else if (id == 4) {
        product.getSetOfProductAttributes().add(new ProductAttribute(null, product.getBrand().getName(), attributeMap.get(39), product));
        product.getSetOfProductAttributes().add(new ProductAttribute(null, "12 tháng", attributeMap.get(37), product));
      }

      product.setSetOfVariationOptions(new HashSet<>());
      for (String variationOptionId : productExcel.getVariations().split(", ")) {
        product.getSetOfVariationOptions().add(variationOptionMap.get(Long.valueOf(variationOptionId)));
      }
      products.add(product);
    }
    productRepository.saveAll(products);
    return true;
  }

  // Temporary use, waiting to be replaced by flyway
  @Transactional
  public boolean insertDataToDatabase() throws IOException, NoSuchFieldException, InvocationTargetException,
      NoSuchMethodException, InstantiationException, IllegalAccessException, ParseException {
    AdministrativeArea area = null;
    Area tempArea;
    if (provinceRepository.count() > 0) {
      throw new RuntimeException("Area initilized");
    }
    InputStream inputStream = ExcelUtil.class.getResourceAsStream(ApplicationConstants.AREAS_FILE);
    Workbook workbook = getWorkbook(inputStream);
    List<AdministrativeArea> areas =
        GetDataFromSheetAndMapToList(AdministrativeArea.class, ApplicationConstants.AREAS_SHEET_NAME, workbook);
    int tempSize = areas.size();
    Map<Long, Province> provinceMap = new HashMap<>();
    Map<Long, District> districtMap = new HashMap<>();
    List<Wards> wards = new ArrayList<>();
    for (int i = 0; i < tempSize; i++) {
      area = areas.get(i);
      if (area.getProvinceId() == 0) {
        continue;
      }
      if (!provinceMap.containsKey(area.getProvinceId())) {
        tempArea = generateArea(area.getProvinceName());
        provinceMap.put(area.getProvinceId(), new Province(area.getProvinceId(), tempArea.getName(), tempArea.getShortName(), tempArea.getType(), tempArea.getPriority(), null));
      }

      if (area.getDistrictId() == 0) {
        continue;
      }
      if (!districtMap.containsKey(area.getDistrictId())) {
        tempArea = generateArea(area.getDistrictName());
        districtMap.put(area.getDistrictId(), new District(area.getDistrictId(), tempArea.getName(), tempArea.getShortName(), tempArea.getType(), tempArea.getPriority(), provinceMap.get(area.getProvinceId()), null));
      }

      if (area.getWardId() == 0) {
        continue;
      }
      tempArea = generateArea(area.getWardName());
      wards.add(new Wards(area.getWardId(), tempArea.getName(), tempArea.getShortName(), tempArea.getType(), tempArea.getPriority(), districtMap.get(area.getDistrictId())));
    }
    provinceRepository.saveAll(provinceMap.values());
    districtRepository.saveAll(districtMap.values());
    wardsRepository.saveAll(wards);
    return true;
  }

  private Area generateArea(String rawName) {
    String processedName = rawName.trim().replaceAll(" +", " ");
    AdministrativeTypeEnum typeEnum = AdministrativeTypeEnum.getAdministrativeTypeEnumByName(processedName);
    if (typeEnum == null) {
      return new Area("", processedName, processedName, 1000);
    }
    String shortName = processedName.split(String.format("(?i)%s ", typeEnum.getName()), 2)[1];
    return new Area(
        typeEnum.getName(),
        String.format("%s %s", typeEnum.getName(), shortName),
        shortName,
        typeEnum.getPriority());
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

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  class Area {
    private String type;
    private String name;
    private String shortName;
    private int priority;
  }
}
