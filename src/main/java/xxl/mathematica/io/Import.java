package xxl.mathematica.io;

import com.google.gson.reflect.TypeToken;
import org.apache.commons.io.IOUtils;
import xxl.codec.Charsets;
import xxl.mathematica.io.excel.AbsExcel;
import xxl.mathematica.io.excel.IExcel;
import xxl.mathematica.single.GsonSingle;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 导入
 */
public class Import {
  /**
   * 默认jxl，支持android
   *
   * @param file
   * @return
   */
  public static List<List<String[]>> importExcel(String file) {
    return importExcel(IExcel.JXL, file);
  }

  /**
   * 导入excel
   *
   * @param method
   * @param file
   * @return
   */
  public static List<List<String[]>> importExcel(int method, String file) {
    try {
      return AbsExcel.getExcelImpl(method).importExcel(file);
    } catch (Exception e) {
      return null;
    }
  }

  /**
   * 以Json的格式导入
   *
   * @param file
   * @return
   */
  public static Map<String, Object> importJson(String file) {
    try {
      return GsonSingle.instance().fromJson(new FileReader(file), new TypeToken<Map<String, Object>>() {
      }.getType());
    } catch (FileNotFoundException e) {
      return null;
    }
  }

  /**
   * 以文本形式导入
   *
   * @param file
   * @return
   */
  public static String importText(String file) {
    try (FileInputStream fis = new FileInputStream(file)) {
      return IOUtils.toString(fis, Charsets.UTF_8);
    } catch (IOException e) {
      return null;
    }
  }
}
