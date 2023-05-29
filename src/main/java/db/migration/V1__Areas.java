package db.migration;

import com.hcmute.tdshop.utils.ExcelUtil;
import org.flywaydb.core.api.migration.BaseJavaMigration;
import org.flywaydb.core.api.migration.Context;
import org.springframework.beans.factory.annotation.Autowired;

public class V1__Areas extends BaseJavaMigration {
  @Autowired
  ExcelUtil excelUtil;

  @Override
  public void migrate(Context context) throws Exception {
    excelUtil.insertDataToDatabase();
  }
}
