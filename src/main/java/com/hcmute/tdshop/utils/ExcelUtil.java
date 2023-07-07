package com.hcmute.tdshop.utils;

import com.hcmute.tdshop.entity.AccountRole;
import com.hcmute.tdshop.entity.Attribute;
import com.hcmute.tdshop.entity.Brand;
import com.hcmute.tdshop.entity.Category;
import com.hcmute.tdshop.entity.District;
import com.hcmute.tdshop.entity.Image;
import com.hcmute.tdshop.entity.Product;
import com.hcmute.tdshop.entity.ProductAttribute;
import com.hcmute.tdshop.entity.ProductStatus;
import com.hcmute.tdshop.entity.Province;
import com.hcmute.tdshop.entity.User;
import com.hcmute.tdshop.entity.VariationOption;
import com.hcmute.tdshop.entity.Wards;
import com.hcmute.tdshop.enums.AccountRoleEnum;
import com.hcmute.tdshop.enums.AdministrativeTypeEnum;
import com.hcmute.tdshop.enums.ProductStatusEnum;
import com.hcmute.tdshop.enums.ProductUserInteractExcel;
import com.hcmute.tdshop.model.AdministrativeArea;
import com.hcmute.tdshop.model.ProductExcel;
import com.hcmute.tdshop.model.UserExcel;
import com.hcmute.tdshop.repository.AccountRoleRepository;
import com.hcmute.tdshop.repository.AttributeRepository;
import com.hcmute.tdshop.repository.BrandRepository;
import com.hcmute.tdshop.repository.CategoryRepository;
import com.hcmute.tdshop.repository.DistrictRepository;
import com.hcmute.tdshop.repository.ProductRepository;
import com.hcmute.tdshop.repository.ProductStatusRepository;
import com.hcmute.tdshop.repository.ProvinceRepository;
import com.hcmute.tdshop.repository.UserRepository;
import com.hcmute.tdshop.repository.VariationOptionRepository;
import com.hcmute.tdshop.repository.WardsRepository;
import com.hcmute.tdshop.utils.annotations.ExcelColumnIndex;
import com.hcmute.tdshop.utils.constants.ApplicationConstants;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
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

  @Autowired
  AccountRoleRepository accountRoleRepository;

  @Autowired
  UserRepository userRepository;

  public Workbook getWorkbook(InputStream inputStream) throws IOException {
    return new XSSFWorkbook(inputStream);
  }

  public <T> List<T> getListObjectFromExcelFile(Class<T> objectClass, String sheetName, MultipartFile listObjectFile)
      throws IOException, NoSuchFieldException, InvocationTargetException, NoSuchMethodException,
      InstantiationException, IllegalAccessException, ParseException {
    Workbook workbook = getWorkbook(listObjectFile.getInputStream());
    return GetDataFromSheetAndMapToList(objectClass, sheetName, workbook);
  }

  public List<ProductUserInteractExcel> generateDataForRecomendationSystem() {
    List<Long> allowedMasterCategory = Arrays.asList(1L, 2L);
    List<Category> laptopCategory = categoryRepository.findByMasterCategory_IdAndParentIsNotNull(1L);
    List<Category> phoneCategory = categoryRepository.findByMasterCategory_IdAndParentIsNotNull(2L);
    Map<Long, List<Category>> mCategoryMap = new HashMap<>();
    mCategoryMap.put(1L, laptopCategory);
    mCategoryMap.put(2L, phoneCategory);
    List<Category> categories = new ArrayList<>();
    List<User> users = userRepository.findAll();

    DistributedRandomNumberGenerator choosenCategoryGenerator;
    DistributedRandomNumberGenerator choosenProductGenerator;
    DistributedRandomNumberGenerator boughtProductGenerator;
    DistributedRandomNumberGenerator ratedProductGenerator;

    // 5* 40% 4* 20% 3* 15% 2* 15% 1* 10%
    DistributedRandomNumberGenerator ratingGenerator = new DistributedRandomNumberGenerator();
    ratingGenerator.addNumber(5, 0.4);
    ratingGenerator.addNumber(4, 0.2);
    ratingGenerator.addNumber(3, 0.15);
    ratingGenerator.addNumber(2, 0.15);
    ratingGenerator.addNumber(1, 0.1);

    Map<Integer, Category> categoryMap = new HashMap<>();
    List<ProductUserInteractExcel> data = new ArrayList<>();
    List<Category> choosenCategories = new ArrayList<>();
    Set<Product> products = new HashSet<>();
    Map<Integer, Product> productMap = new HashMap<>();
    List<Product> choosenProducts = new ArrayList<>();
    Map<Integer, Product> choosenProductMap = new HashMap<>();
    List<Product> boughtProducts = new ArrayList<>();
    Map<Integer, Product> boughtProductMap = new HashMap<>();
    List<Product> ratedProducts = new ArrayList<>();

    int mCateSize = 0;
    int productSize = 0;
    int pickedCategories = 0;
    int pickedProducts = 0;
    int randNum = 0;
    int i = 1;
    int limit = 0;
    Long orderId = 1L;
    int numOrder;
    for (User user : users) {
//      numOrder = getRandomNumber(5, 9);
      numOrder = 8;
      while (numOrder != 0) {
        // Pick master category
        choosenCategoryGenerator = new DistributedRandomNumberGenerator();
        categoryMap.clear();
        categories = mCategoryMap.get((long) getRandomNumber(1, 3));
        mCateSize = categories.size();
        for (i = 1; i <= mCateSize; i++) {
          categoryMap.put(i, categories.get(i - 1));
          choosenCategoryGenerator.addNumber(i, 1.0 / mCateSize);
        }

        // reset generators
        choosenProductGenerator = new DistributedRandomNumberGenerator();
        boughtProductGenerator = new DistributedRandomNumberGenerator();
        ratedProductGenerator = new DistributedRandomNumberGenerator();

        // Clear all product
        choosenCategories.clear();
        products.clear();
        productMap.clear();
        choosenProducts.clear();
        choosenProductMap.clear();
        boughtProducts.clear();
        boughtProductMap.clear();
        ratedProducts.clear();

        // Choose 2 - 4 category
        limit = getRandomNumber(2, 5);
        pickedCategories = 0;
        while (pickedCategories != limit) {
          randNum = choosenCategoryGenerator.getDistributedRandomNumber();
          if (randNum != 0) {
            choosenCategories.add(categoryMap.get(randNum));
            pickedCategories++;
          }
        }

        // Choose 25 - 40 product
        for (Category category : choosenCategories) {
          products.addAll(category.getSetOfProducts());
        }
        productSize = products.size();
        i = 1;
        for (Product product : products) {
          productMap.put(i, product);
          choosenProductGenerator.addNumber(i, 1.0 / productSize);
          i++;
        }
        limit = getRandomNumber(20, 41);
        while (pickedProducts != limit) {
          randNum = choosenProductGenerator.getDistributedRandomNumber();
          if (randNum != 0) {
            choosenProducts.add(productMap.get(randNum));
            pickedProducts++;
          }
        }

        // Choose 5 bought product
        i = 1;
        productSize = choosenProducts.size();
        for (Product product : choosenProducts) {
          choosenProductMap.put(i, product);
          boughtProductGenerator.addNumber(i, 1.0 / productSize);
          i++;
        }
        pickedProducts = 0;
        while (pickedProducts != 5) {
          randNum = boughtProductGenerator.getDistributedRandomNumber();
          if (randNum != 0) {
            boughtProducts.add(choosenProductMap.get(randNum));
            pickedProducts++;
          }
        }

        // Choose 2 rated product
        i = 1;
        productSize = boughtProducts.size();
        for (Product product : boughtProducts) {
          boughtProductMap.put(i, product);
          ratedProductGenerator.addNumber(i, 1.0 / productSize);
          i++;
        }
        pickedProducts = 0;
        while (pickedProducts != 2) {
          randNum = ratedProductGenerator.getDistributedRandomNumber();
          if (randNum != 0) {
            ratedProducts.add(boughtProductMap.get(randNum));
            pickedProducts++;
          }
        }

        for (Product product : choosenProducts) {
          data.add(
              new ProductUserInteractExcel
                  (
                      orderId,
                      user.getId(),
                      product.getId(),
                      StringUtils.join(choosenCategories.stream().map(Category::getId).collect(Collectors.toList()), ", "),
                      boughtProducts.contains(product) ? 1 : 0,
                      ratedProducts.contains(product) ? ratingGenerator.getDistributedRandomNumber() : 0
                  )
          );
        }
        numOrder--;
        orderId++;
      }
    }
    System.out.println(data.size());
    return data;
  }

  public int getRandomNumber(int min, int max) {
    return (int) ((Math.random() * (max - min)) + min);
  }

  @Transactional
  public boolean insertUsersToDatabase() throws IOException, NoSuchFieldException, InvocationTargetException,
      NoSuchMethodException, InstantiationException, IllegalAccessException, ParseException {
    InputStream inputStream = ExcelUtil.class.getResourceAsStream(ApplicationConstants.USERS_FILE);
    Workbook workbook = getWorkbook(inputStream);
    List<UserExcel> userExcels =
        GetDataFromSheetAndMapToList(UserExcel.class, ApplicationConstants.USERS_SHEET_NAME, workbook);
    AccountRole role = accountRoleRepository.findById(AccountRoleEnum.ROLE_USER.getId()).get();
    UserExcel userExcel;
    List<User> users = new ArrayList<>();
    User user;
    LocalDateTime now = LocalDateTime.now();
    int size = userExcels.size();
    for (int i = 0; i < size; i++) {
      userExcel = userExcels.get(i);
      user = new User();
      user.setFirstName(userExcel.getFirstName());
      user.setLastName(userExcel.getLastName());
      user.setEmail(userExcel.getEmail());
      user.setPhone(userExcel.getPhone());
      user.setBirthdate(userExcel.getBirthdate());
      user.setGender(userExcel.getGender());
      user.setUsername(userExcel.getUsername());
      user.setPassword(userExcel.getPassword());
      user.setRole(role);
      user.setIsActive(true);
      user.setIsVerified(true);
      user.setCreatedAt(now);
      users.add(user);
    }
    userRepository.saveAll(users);
    return true;
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
        product.getSetOfProductAttributes()
            .add(new ProductAttribute(null, product.getBrand().getName(), attributeMap.get(1L), product));
        product.getSetOfProductAttributes().add(new ProductAttribute(null, "12 tháng", attributeMap.get(2L), product));
      } else if (id == 2) {
        product.getSetOfProductAttributes()
            .add(new ProductAttribute(null, product.getBrand().getName(), attributeMap.get(47L), product));
        product.getSetOfProductAttributes().add(new ProductAttribute(null, "12 tháng", attributeMap.get(48L), product));
      } else if (id == 4) {
        product.getSetOfProductAttributes()
            .add(new ProductAttribute(null, product.getBrand().getName(), attributeMap.get(39L), product));
        product.getSetOfProductAttributes().add(new ProductAttribute(null, "12 tháng", attributeMap.get(37L), product));
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
        provinceMap.put(area.getProvinceId(),
            new Province(area.getProvinceId(), tempArea.getName(), tempArea.getShortName(), tempArea.getType(),
                tempArea.getPriority(), null));
      }

      if (area.getDistrictId() == 0) {
        continue;
      }
      if (!districtMap.containsKey(area.getDistrictId())) {
        tempArea = generateArea(area.getDistrictName());
        districtMap.put(area.getDistrictId(),
            new District(area.getDistrictId(), tempArea.getName(), tempArea.getShortName(), tempArea.getType(),
                tempArea.getPriority(), provinceMap.get(area.getProvinceId()), null));
      }

      if (area.getWardId() == 0) {
        continue;
      }
      tempArea = generateArea(area.getWardName());
      wards.add(new Wards(area.getWardId(), tempArea.getName(), tempArea.getShortName(), tempArea.getType(),
          tempArea.getPriority(), districtMap.get(area.getDistrictId())));
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
    DateTimeFormatter dateFormatter = new DateTimeFormatterBuilder().appendPattern("yyyy-MM-dd").toFormatter();
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
        } else if (objectFieldType.equals(LocalDate.class)) {
          // java.util.LocalDate
          objectField.set(object, LocalDate.parse(value, dateFormatter));
        } else if (objectFieldType.equals(Boolean.class)) {
          objectField.set(object, Boolean.valueOf(value));
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
