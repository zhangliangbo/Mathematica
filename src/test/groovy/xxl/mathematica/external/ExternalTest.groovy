package xxl.mathematica.external

import xxl.mathematica.RandomChoice
import xxl.mathematica.RandomInteger
import xxl.mathematica.Table
import xxl.mathematica.io.Export

class ExternalTest extends GroovyTestCase {
    void testExec() {
        println(new String(External.runProcess(new File("C:\\Users\\zhang\\Desktop\\file_server_main_jar"), 'mathematica.exe'), 'GBK'))
    }

    void testRun() {
        println(External.run('git --help'))
    }

    void testSendMail() {
        println(External.sendMail("forbidden@aliyun.com", "附件", "<img src=\"https://himg2.huanqiucdn.cn/attachment2010/2019/1101/20191101072831653.jpg\"/>", [new File("C:\\Users\\zhang\\Desktop\\shu.jpg")], "2472110501@qq.com", "smtp.aliyun.com", "forbidden@aliyun.com", "forbidden"))
    }

    void testExportXlsx() {
        Export.exportXlsx("C:\\Users\\zhang\\Desktop\\shu.xls", Table.table({ t ->
            return new Pojo("姓名" + t, RandomInteger.randomInteger(100), RandomChoice.randomChoice(["男", "女"]))
        }, 10))
    }


    void testExportXlsxWithJxl() {
        Export.exportXlsx("C:\\Users\\zhang\\Desktop\\jxl.xlsx", Table.table({ t ->
            return new Pojo("姓名" + (t + 1), RandomInteger.randomInteger(100), RandomChoice.randomChoice(["男", "女"]))
        }, 100))
    }
}
