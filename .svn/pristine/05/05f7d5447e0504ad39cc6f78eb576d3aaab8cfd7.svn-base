package test;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class TestStream {

    @Test
    public void test1(){
        List<String> names = new ArrayList<>();
        names.add("zhangsan");
        names.add("lisi");
        names.add("yu");

        List<String> collect = names.stream().filter(x -> x.length() > 3).collect(Collectors.toList());
        collect.forEach(x -> System.out.println(x));
    }

    @Test
    public void test2(){
        List<String> names = new ArrayList<>();
        names.add("zhangsan");
        names.add("lisi");
        names.add("yu");

        List<Integer> collect = names.stream().map(x -> x.length()).collect(Collectors.toList());
        collect.forEach(x -> System.out.println(x));
    }

    @Test
    public void test3(){
        String ids = "12,45,78,89,56,23,14,4,47,58,65,32,19";
        String[] idArr = ids.split(",");
        List<Long> collect = Arrays.stream(idArr).map(x -> Long.parseLong(x)).collect(Collectors.toList());
        collect.forEach(x -> System.out.println(x));
    }

    @Test
    public void test4(){
        String ids = "12,45,78,89,56,23,14,4,47,58,65,32,19";
        List<Long> collect = Arrays.stream(ids.split(","))
                .map(x -> Long.parseLong(x)).filter(x -> x > 23).collect(Collectors.toList());
        collect.forEach(x -> System.out.println(x));
    }

    @Test
    public void test5(){
        String ids = "12,45,78,89,56,23,14,4,47,58,65,32,19";
        List<Long> list = Arrays.stream(ids.split(","))
                .map(x -> Long.parseLong(x)).filter(x -> x > 23)
                .map(x -> x + 5).collect(Collectors.toList());
        list.forEach(x -> System.out.println(x));
    }

    @Test
    public void test6(){
        String ids = "12,45,78,89,56,23,14,4,47,58,65,32,19";
        List<Long> list = Arrays.stream(ids.split(","))
                .map(x -> Long.parseLong(x)).sorted().collect(Collectors.toList());
        list.forEach(x -> System.out.println(x));
    }

    @Test
    public void test7(){
        String ids = "12,45,78,89,56,23,14,4,47,58,65,32,19";
        List<Long> collect = Arrays.stream(ids.split(","))
                .map(x -> Long.parseLong(x)).sorted().distinct().collect(Collectors.toList());
        collect.forEach(x -> System.out.println(x));
    }

    @Test
    public void test8(){
        String ids = "12,45,78,89,56,23,14,4,47,58,65,32,19";
        long count = Arrays.stream(ids.split(","))
                .map(x -> Long.parseLong(x)).distinct().count();
        System.out.println(count);
    }

    @Test
    public void test9(){
        List<Book> books = new ArrayList<>();

        Book b1 = new Book();
        b1.setBookName("三国");
        b1.setBookPrice(20);

        Book b2 = new Book();
        b2.setBookName("西游");
        b2.setBookPrice(30);

        Book b3 = new Book();
        b3.setBookName("西游66");
        b3.setBookPrice(33);

        Book b4 = new Book();
        b4.setBookName("西游88");
        b4.setBookPrice(99);

        books.add(b1);
        books.add(b2);
        books.add(b3);
        books.add(b4);

        List<Book> collect = books.stream().skip(1).limit(2).collect(Collectors.toList());
        System.out.println(collect);
    }

    @Test
    public void test10(){
        List<Book> books = new ArrayList<>();

        Book b1 = new Book();
        b1.setBookName("三国");
        b1.setBookPrice(20);

        Book b2 = new Book();
        b2.setBookName("西游");
        b2.setBookPrice(30);

        Book b3 = new Book();
        b3.setBookName("西游66");
        b3.setBookPrice(33);

        Book b4 = new Book();
        b4.setBookName("西游88");
        b4.setBookPrice(99);

        books.add(b1);
        books.add(b2);
        books.add(b3);
        books.add(b4);

        //求价格最贵的那本书
        Book book = books.stream().max((x, y) -> (x.getBookPrice() - y.getBookPrice())).get();
        //求价格最便宜的那本书
        Book book1 = books.stream().min((x, y) -> (x.getBookPrice() - y.getBookPrice())).get();
        System.out.println(book);
        System.out.println(book1);
    }

    @Test
    public void test11(){
        List<Book> books = new ArrayList<>();

        Book b1 = new Book();
        b1.setBookName("三国");
        b1.setBookPrice(20);

        Book b2 = new Book();
        b2.setBookName("西游");
        b2.setBookPrice(30);

        Book b3 = new Book();
        b3.setBookName("西游66");
        b3.setBookPrice(33);

        Book b4 = new Book();
        b4.setBookName("西游88");
        b4.setBookPrice(99);

        books.add(b1);
        books.add(b2);
        books.add(b3);
        books.add(b4);

        
    }

}
