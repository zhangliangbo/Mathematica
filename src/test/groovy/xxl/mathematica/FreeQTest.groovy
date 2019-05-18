package xxl.mathematica

import xxl.mathematica.function.Predicate
import xxl.mathematica.predication.FreeQ
import xxl.mathematica.function.Function

import org.junit.Test

/**
 * Created by zhang on 2017/9/10.
 */

class FreeQTest {

    class Student {
        String name
    }

    @Test
    void name() throws Exception {
        final List<String> names = ["张三", "李四", "王五"]
        List<Student> studentList = Array.array(new Function<Integer, Student>() {
            @Override
            Student apply(Integer integer) {
                Student student = new Student()
                student.name = names.get(integer)
                return student
            }
        }, names.size())

        System.out.println("你们班\"风清扬\"是不是转校了? " +
                (FreeQ.freeQ(studentList, new Predicate<Student>() {
                    @Override
                    boolean test(Student student) {
                        return student.name.equals("风清扬")
                    }
                }) ? "是的" : "还没"))
    }
}