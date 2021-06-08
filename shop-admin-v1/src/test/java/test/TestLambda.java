package test;

import org.junit.Test;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

public class TestLambda implements Serializable {

    @Test
    public void test1(){

        List<Book> bookList = new ArrayList<>();

        Book book1 = new Book();
        book1.setBookName("三国");
        book1.setBookPrice(26);

        Book book2 = new Book();
        book2.setBookName("西游");
        book2.setBookPrice(33);

        Book book3 = new Book();
        book3.setBookName("水浒");
        book3.setBookPrice(28);

        bookList.add(book1);
        bookList.add(book2);
        bookList.add(book3);

        bookList.forEach(x -> System.out.println(x.getBookName()+"===="+x.getBookPrice()));

    }

    @Test
    public void test2(){
        Map<String,String> map = new HashMap<>();
        map.put("一","1");
        map.put("二","2");
        map.put("三","3");
        map.put("四","4");
        map.put("五","5");
        map.put("六","6");
        map.put("七","7");

        map.forEach((k,v) -> System.out.println(k+":"+v));
    }

    @Test
    public void test3(){
        List<Map<String,String>> mapList = new ArrayList<>();
        Map<String,String> map1 = new HashMap<>();
        map1.put("yi","1");
        map1.put("er","2");
        Map<String,String> map2 = new HashMap<>();
        map2.put("san","3");
        map2.put("si","4");
        Map<String,String> map3 = new HashMap<>();
        map3.put("wu","5");
        map3.put("liu","6");
        mapList.add(map1);
        mapList.add(map2);
        mapList.add(map3);

        mapList.forEach(x -> x.forEach((k,v) -> System.out.println(k+":"+v)));
    }

    @Test
    public void test4(){
        String str = "12,45,78,89,56,23,14,47,58,25,36,69";
        List<Long> collect = Arrays.stream(str.split(",")).map(x -> Long.parseLong(x)).sorted().collect(Collectors.toList());
        collect.forEach(x -> System.out.println(x));
    }

    @Test
    public void test5(){
        List<Map<String,String>> list = new ArrayList<>();
        Map<String,String> map1  = new HashMap<>();
        map1.put("张三1","24");
        map1.put("李四1","33");
        map1.put("王五1","42");
        map1.put("赵六1","51");
        Map<String,String> map2  = new HashMap<>();
        map2.put("张三2","25");
        map2.put("李四2","36");
        map2.put("王五2","47");
        map2.put("赵六2","58");
        Map<String,String> map3  = new HashMap<>();
        map3.put("张三3","30");
        map3.put("李四3","99");
        map3.put("王五3","88");
        map3.put("赵六3","12");
        list.add(map1);
        list.add(map2);
        list.add(map3);

        list.removeIf(x -> x.equals(map2));
        list.forEach(x -> System.out.println(x));
    }

    @Test
    public void test6(){
        Map<String,String> map1 = new HashMap<>();
        map1.put("name","zhangsan");
        map1.put("sex","男");
        map1.put("age","30");

        Map<String,String> map2 = new HashMap<>();
        map2.put("name","lisi");
        map2.put("sex","女");
        map2.put("age","26");

        Map<String,String> map3 = new HashMap<>();
        map3.put("name","wangwu");
        map3.put("sex","男");
        map3.put("age","22");

        List<Map> userList = new ArrayList<>();
        userList.add(map1);
        userList.add(map2);
        userList.add(map3);

        userList.forEach(x -> x.forEach((x1,x2) -> System.out.println(x1+"====="+x2)));
    }

    @Test
    public void test7(){
        List<String> names = new ArrayList<>();
        names.add("zhengsan");
        names.add("lisi");
        names.add("wangyuan");
        names.add("lisi");

        names.removeIf(x -> x.equals("lisi"));
        names.forEach(x -> System.out.println(x));
    }

    @Test
    public void test8(){
        List<Book> books = new ArrayList<>();

        Book b1 = new Book();
        b1.setBookName("三国");
        b1.setBookPrice(20);

        Book b2 = new Book();
        b2.setBookName("西游");
        b2.setBookPrice(30);

        Book b3 = new Book();
        b3.setBookName("水浒");
        b3.setBookPrice(22);

        books.add(b1);
        books.add(b2);
        books.add(b3);

        books.removeIf(x -> x.getBookPrice() > 20 && x.getBookPrice() <= 25);
        books.forEach(x -> System.out.println(x.getBookName()+"==="+x.getBookPrice()));
    }

}
