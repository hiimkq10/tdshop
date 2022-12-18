package com.hcmute.tdshop.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.hcmute.tdshop.dto.brand.AddBrandRequest;
import com.hcmute.tdshop.dto.brand.UpdateBrandRequest;
import com.hcmute.tdshop.entity.Brand;
import com.hcmute.tdshop.mapper.BrandMapper;
import com.hcmute.tdshop.model.DataResponse;
import com.hcmute.tdshop.repository.BrandRepository;
import com.hcmute.tdshop.repository.ProductRepository;
import com.hcmute.tdshop.service.BrandService;
import com.hcmute.tdshop.utils.constants.ApplicationConstants;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Optional;
import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@Slf4j
public class BrandServiceImpl implements BrandService {

  @Value("${CLOUDINARY_URL}")
  private String cloudinaryURL;

  @Value("${cloudinaryBrandImagePath}")
  private String imagePath;

  private Cloudinary cloudinary;

  @Autowired
  private BrandRepository brandRepository;

  @Autowired
  private ProductRepository productRepository;

  @Autowired
  private BrandMapper brandMapper;

  @PostConstruct
  private void postConstruct() {
    log.error(cloudinaryURL);
//    this.cloudinary = new Cloudinary(cloudinaryURL);
//    this.cloudinary.config.secure = true;
  }

  @Override
  public DataResponse getAllBrand(Pageable page) {
    Page<Brand> brands = brandRepository.findAll(page);
    return new DataResponse(brands);
  }

  @Override
  public DataResponse getBrandById(long id) {
    Optional<Brand> optionalBrand = brandRepository.findById(id);
    return optionalBrand.map(DataResponse::new).orElseGet(
        () -> new DataResponse(ApplicationConstants.BAD_REQUEST, ApplicationConstants.BRAND_NOT_FOUND,
            ApplicationConstants.BAD_REQUEST_CODE));
  }

  @Override
  public DataResponse insertBrand(AddBrandRequest request, MultipartFile logo) {
    Brand brand = brandMapper.AddBrandRequestToBrand(request);
    if (checkIfNameExisted(brand.getName())) {
      return new DataResponse(ApplicationConstants.BAD_REQUEST, ApplicationConstants.BRAND_NAME_EXISTED, ApplicationConstants.BAD_REQUEST_CODE);
    }
    if (logo != null) {
      String url = uploadBrandImage(logo);
      if (url != null) {
        brand.setLogoUrl(url);
      }
    }
    brand = brandRepository.save(brand);
    return new DataResponse(ApplicationConstants.BRAND_ADD_SUCCESSFULLY, brand);
  }

  @Override
  public DataResponse updateBrand(long id, UpdateBrandRequest request, MultipartFile logo) {
    Brand brandToUpdate = brandMapper.UpdateBrandRequestToBrand(request);
    Optional<Brand> optionalBrand = brandRepository.findById(id);
    if (optionalBrand.isPresent()) {
      Brand currentBrand = optionalBrand.get();
      if (brandToUpdate.getName() != null) {
        if (!checkIfNameExisted(brandToUpdate.getName())) {
          currentBrand.setName(brandToUpdate.getName());
        }
      }
      if (logo != null) {
        String url;
        if (currentBrand.getLogoUrl() != null) {
          url = updateBrandImage(logo, currentBrand.getLogoUrl());
        } else {
          url = uploadBrandImage(logo);
        }
        currentBrand.setLogoUrl(url);
      }
      currentBrand = brandRepository.saveAndFlush(currentBrand);
      return new DataResponse(ApplicationConstants.BRAND_UPDATE_SUCCESSFULLY, currentBrand);
    }
    return new DataResponse(ApplicationConstants.BAD_REQUEST, ApplicationConstants.BRAND_NOT_FOUND,
        ApplicationConstants.BAD_REQUEST_CODE);
  }

  @Override
  public DataResponse deleteBrand(long id) {
    Optional<Brand> optionalBrand = brandRepository.findById(id);
    if (optionalBrand.isPresent()) {
      Brand currentBrand = optionalBrand.get();
      if (currentBrand.getLogoUrl() != null) {
        deleteBrandImage(currentBrand.getLogoUrl());
      }
      brandRepository.delete(currentBrand);
      return new DataResponse(ApplicationConstants.BRAND_DELETE_SUCCESSFULLY, true);
    }
    return new DataResponse(ApplicationConstants.BAD_REQUEST, ApplicationConstants.BRAND_NOT_FOUND,
        ApplicationConstants.BAD_REQUEST_CODE);
  }

  private String uploadBrandImage(MultipartFile image) {
    this.cloudinary = new Cloudinary(cloudinaryURL);
    this.cloudinary.config.secure = true;
    try {
      String originalFilename = image.getOriginalFilename();
      String fileName = generateFileName(originalFilename, 1);
      Map params = ObjectUtils.asMap(
          "use_filename", true,
          "unique_filename", true,
          "overwrite", false,
          "resource_type", "raw",
          "public_id", imagePath + "/" + fileName
      );
      Map result = cloudinary.uploader().upload(image.getBytes(), params);
      return result.get("secure_url").toString();
    } catch (IOException exception) {
      return null;
    }
  }

  private String updateBrandImage(MultipartFile image, String imageUrl) {
    this.cloudinary = new Cloudinary(cloudinaryURL);
    this.cloudinary.config.secure = true;
    String publicId = findImagePublicId(imageUrl);
    try {
      String originalFilename = image.getOriginalFilename();
      if (originalFilename == null) {
        return null;
      }
      String fileName = generateFileName(originalFilename, 1);
      Map params = ObjectUtils.asMap(
          "use_filename", true,
          "unique_filename", true,
          "overwrite", true,
          "resource_type", "raw",
          "public_id", imagePath + "/" + publicId
      );
      Map result = cloudinary.uploader().upload(image.getBytes(), params);
      return result.get("secure_url").toString();
    } catch (IOException exception) {
      return null;
    }
  }

  private void deleteBrandImage(String imageUrl) {
    this.cloudinary = new Cloudinary(cloudinaryURL);
    this.cloudinary.config.secure = true;
    String publicId = findImagePublicId(imageUrl);
    try {
      cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
    } catch (IOException exception) {

    }
  }

  private String generateFileName(String orginalFileName, int offset) {
    LocalDateTime now = LocalDateTime.now();
    String dateTimePattern = "yyyyMMddHHmmss";
    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(dateTimePattern);
    String[] arrOfOriginalFileName = orginalFileName.split("\\.");
    String fileName = arrOfOriginalFileName[0];
    String fileSuffix = arrOfOriginalFileName[1];
    return fileName + "_" + now.format(dateTimeFormatter) + "_" + String.valueOf(offset) + "." + fileSuffix;
  }

  private String findImagePublicId(String url) {
    return url.substring(url.indexOf(imagePath));
  }

  private boolean checkIfNameExisted(String name) {
    return brandRepository.existsByNameIgnoreCase(name);
  }
}
