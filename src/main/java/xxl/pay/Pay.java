package xxl.pay;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradeCancelRequest;
import com.alipay.api.request.AlipayTradePrecreateRequest;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.response.AlipayTradeCancelResponse;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.alipay.api.response.AlipayTradeRefundResponse;
import xxl.mathematica.io.ExportString;
import xxl.wxpay.WXPay;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * 支付
 */
public class Pay {

  private static Map<String, AlipayClient> aliMap = new HashMap<>();
  private static Map<String, WXPay> payMap = new HashMap<>();

  /**
   * 获取下单二维码
   *
   * @param appId          应用ID
   * @param outTradeNo     商家交易单号
   * @param totalAmount    总金额，以分为单位
   * @param subject        商品明细
   * @param storeId        商家门店编号
   * @param timeoutExpress 交易超时时间
   * @return
   */
  public static AlipayTradePrecreateResponse aliBarcode(String appId, String outTradeNo, long totalAmount, String subject, String storeId, String timeoutExpress, String notifyUrl) {
    AlipayTradePrecreateRequest request = new AlipayTradePrecreateRequest();
    request.setNotifyUrl(notifyUrl);
    Map<String, String> map = new HashMap<>();
    map.put("out_trade_no", outTradeNo);
    map.put("total_amount", String.format(Locale.CHINA, "%.2f", totalAmount / 100f));
    map.put("subject", subject);
    map.put("store_id", storeId);
    map.put("timeout_express", timeoutExpress);
    request.setBizContent(ExportString.exportStringJson(map));
    try {
      return getAli(appId).execute(request);
    } catch (AlipayApiException e) {
      return null;
    }
  }

  /**
   * 取消订单
   *
   * @param appId
   * @param tradeNo
   * @param outTradeNo
   * @param notifyUrl
   * @return
   */
  public static AlipayTradeCancelResponse aliOrderCancel(String appId, String tradeNo, String outTradeNo, String notifyUrl) {
    AlipayTradeCancelRequest request = new AlipayTradeCancelRequest();
    request.setNotifyUrl(notifyUrl);
    Map<String, String> map = new HashMap<>();
    if (tradeNo != null) {
      map.put("trade_no", tradeNo);
    } else {
      map.put("out_trade_no", outTradeNo);
    }
    try {
      return getAli(appId).execute(request);
    } catch (AlipayApiException e) {
      return null;
    }
  }

  /**
   * 根据阿里订单号或者商家订单号查询订单
   *
   * @param appId
   * @param tradeNo    阿里订单号
   * @param outTradeNo 商家订单号
   * @return
   */
  public static AlipayTradeQueryResponse aliOrderQuery(String appId, String tradeNo, String outTradeNo, String notifyUrl) {
    AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
    request.setNotifyUrl(notifyUrl);
    Map<String, String> map = new HashMap<>();
    if (tradeNo != null) {
      map.put("trade_no", tradeNo);
    } else {
      map.put("out_trade_no", outTradeNo);
    }
    request.setBizContent(ExportString.exportStringJson(map));
    try {
      return getAli(appId).execute(request);
    } catch (AlipayApiException e) {
      return null;
    }
  }

  /**
   * 根据阿里订单号或者商家订单号退款
   *
   * @param appId
   * @param tradeNo
   * @param outTradeNo
   * @param notifyUrl
   * @return
   */
  public static AlipayTradeRefundResponse aliOrderRefund(String appId, String tradeNo, String outTradeNo, String notifyUrl) {
    AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
    request.setNotifyUrl(notifyUrl);
    Map<String, String> map = new HashMap<>();
    if (tradeNo != null) {
      map.put("trade_no", tradeNo);
    } else {
      map.put("out_trade_no", outTradeNo);
    }
    request.setBizContent(ExportString.exportStringJson(map));
    try {
      return getAli(appId).execute(request);
    } catch (AlipayApiException e) {
      return null;
    }
  }

  /**
   * 注册阿里平台信息
   *
   * @param serverUrl
   * @param appId
   * @param privateKey
   * @param format
   * @param charset
   * @param publicKey
   * @param signType
   */
  public static void registerAli(String serverUrl, String appId, String privateKey, String format, String charset, String publicKey, String signType) {
    AlipayClient client = aliMap.get(appId);
    if (client == null) {
      aliMap.put(appId, new DefaultAlipayClient(serverUrl, appId, privateKey, format, charset, publicKey, signType));
    }
  }

  /**
   * 注册微信平台信息
   *
   * @param mchId     商户平台账号
   * @param certFile  证书文件
   * @param appId     公众平台账号
   * @param key       商户平台校验值
   * @param notifyUrl 通知回调地址
   */
  public static void registerWx(String mchId, String certFile, String appId, String key, String notifyUrl) {
    WXPay wxPay = payMap.get(mchId);
    if (wxPay == null) {
      payMap.put(mchId, new WXPay(new SimpleWxConfig(appId, certFile, key, mchId), notifyUrl, false));
    }
  }

  /**
   * 微信支付二维码
   *
   * @param mchId
   * @param ip
   * @param orderSim
   * @param goods
   * @param money
   * @return
   */
  public static Map<String, String> wxBarcode(String mchId, String ip, String orderSim, String goods, long money) {
    Map<String, String> map = new HashMap<>();
    map.put("body", goods);
    map.put("out_trade_no", orderSim);
    map.put("total_fee", String.valueOf(money));
    map.put("spbill_create_ip", ip);
    map.put("trade_type", "NATIVE");
    try {
      return getWxPay(mchId).unifiedOrder(map);
    } catch (Exception e) {
      return null;
    }
  }

  /**
   * 根据微信订单号查询
   *
   * @param mchId
   * @param mchNum
   * @return
   */
  public static Map<String, String> wxOrderQueryByMchNum(String mchId, String mchNum) {
    try {
      Map<String, String> map = new HashMap<>();
      map.put("out_trade_no", mchNum);
      return getWxPay(mchId).orderQuery(map);
    } catch (Exception e) {
      return null;
    }
  }

  /**
   * 根据商户订单号查询
   *
   * @param mchId
   * @param wxNum
   * @return
   */
  public static Map<String, String> wxOrderQueryByWxNum(String mchId, String wxNum) {
    try {
      Map<String, String> map = new HashMap<>();
      map.put("transaction_id", wxNum);
      return getWxPay(mchId).orderQuery(map);
    } catch (Exception e) {
      return null;
    }
  }

  private static AlipayClient getAli(String appId) {
    AlipayClient ali = aliMap.get(appId);
    if (ali == null) {
      throw new IllegalArgumentException("请先使用registerAli注册支付宝平台信息");
    }
    return ali;
  }

  private static WXPay getWxPay(String mchId) {
    WXPay wxPay = payMap.get(mchId);
    if (wxPay == null) {
      throw new IllegalArgumentException("请先使用registerWx注册微信平台信息");
    }
    return wxPay;
  }
}