package xxl.mathematica.io

import xxl.mathematica.io.excel.IExcel

class ImportTest extends GroovyTestCase {
    void testImportExcel() {
        println(Import.importExcel(IExcel.POI, "C:\\Users\\zhang\\Desktop\\helloworld.xlsx"))
    }
}
