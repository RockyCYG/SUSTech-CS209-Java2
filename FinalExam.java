import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.*;
import java.util.stream.IntStream;

@SuppressWarnings("all")
public class Main {
    public static void main(String[] args) throws Exception {
        List<Number> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        list.add(9);
        list.add(6);
        list.add(1000000);
        list.add(100);
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i) == null) {
                list.remove(i);
            }
        }
        System.out.println(list);
        Consumer<Number> consumer = System.out::println;
        list.forEach(consumer);
        Supplier<String> supplier = new Supplier<String>() {
            @Override
            public String get() {
                return "Java";
            }
        };
        String s = supplier.get();
        System.out.println(s);
        list.stream().sorted(new Comparator<Number>() {
            @Override
            public int compare(Number o1, Number o2) {
                return 0;
            }
        }).forEach(System.out::println);
        list.stream().findFirst().ifPresent(System.out::println);
        list.stream().reduce(((number, number2) -> {
            return number.intValue() + number.intValue();
        }));
        List<String> words = Arrays.asList("Java", "Python", "C", "C++", "JavaScript");
        Optional<String> x = words.stream()
                .reduce((word1, word2) -> word1.length() > word2.length() ? word1 : word2);
        System.out.println(x.get());
        System.out.println(Thread.currentThread().getName());
        Callable<Integer> callable = new MyCallable();
        FutureTask<Integer> futureTask = new FutureTask<>(callable);
        Thread thread = new Thread(futureTask);
        thread.start();
        try {
            System.out.println(futureTask.get());
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
        String result = words.stream().reduce((s1, s2) -> s1 + s2).get();
        System.out.println(result);
        StringBuilder s1 = new StringBuilder("Hello");
        StringBuilder s2 = s1;
        s2.append("a");
        System.out.println(s1);
        System.out.println(s2);
        String s3 = "hello";
        String s4 = "helloosadf";
        char ch = '\0';
        System.out.println((int) ch);
        System.out.println(s3.compareTo(s4));

        A b = new B(15);
        B b2 = (B) b;
        System.out.println(b.a);
        b.af();
        Complex c1 = new Complex();
        Complex c2 = new Complex();

        try {
            System.out.println(Class.forName("java.lang.String"));
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        A a = new A();
        Class<? extends A> aClass = a.getClass();
        Field[] fields = aClass.getDeclaredFields();
        System.out.println(Arrays.toString(fields));
        System.out.println(Arrays.toString(aClass.getMethods()));
        for (Field field : fields) {
            Range annotation = field.getAnnotation(Range.class);
            if (annotation == null) continue;
            System.out.println(annotation.max());
            System.out.println(field.getAnnotation(Range.class));
        }
        Range annotation = aClass.getAnnotation(Range.class);
        System.out.println(annotation.max());
        try {
            System.out.println(a.getX());
            Field xField = aClass.getDeclaredField("x");
            xField.setAccessible(true);
            xField.setInt(a, 1000);
            System.out.println(a.getX());
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        display(1, 2, "3352112dlkfjas", 1000);
        List<Integer> newList = words.stream().map(String::length).toList();
        System.out.println(newList);
        Finder finder = String::indexOf;
        finder = (String p, String q) -> p.indexOf(q);
        System.out.println(finder.find("abcd", "c"));
        ArrayList arr = new ArrayList();
        arr.add(1);
        addElement(arr);
        System.out.println(arr.get(1));
        Finder f = (m, n) -> 0;
        int[] array = new int[2];
        array[0] = 10;
        addElement(array);
        System.out.println(Arrays.toString(array));
        System.out.println(words);
        words.stream().peek(System.out::println).toList();
        List<String> stringArr = Arrays.asList("123", "456");
        int index = stringArr.indexOf(1);
        stringArr.forEach(s5 -> System.out.println(s5 + ""));
        stringArr.forEach(System.out::println);
        OptionalDouble average = stringArr.stream().mapToInt(Integer::parseInt).average();
        average.ifPresent(value -> System.out.println(value));
        System.out.println(average);
        stringArr.stream().sorted((o1, o2) -> {
            return o1.compareTo(o2);
        });
        int reduce = IntStream.of(5, 6, 7, 8).reduce(1, ((left, right) -> left * right));
        System.out.println(reduce);
        System.out.println(Direction.NORTH.compareTo(Direction.WEST));
        int[][] arr1 = new int[2][];
        arr1[0] = new int[]{1};
        arr1[1] = new int[]{2, 3};
        Finder.print();
        List<A> aList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            aList.add(new A());
        }
        System.out.println(aList);
        aList.stream().forEach((A a1) -> {
            a1 = new A();
            a1.a = 10001;
        });
        System.out.println(aList);
        List<Integer> numList = new ArrayList<>();
        numList.add(1);
        numList.add(2);
        numList.add(3);
        List<Integer> integers = numList.stream().map(integer -> integer + 1).toList();
        System.out.println(numList);
        System.out.println(integers);
        ListIterator<Integer> integerListIterator = integers.listIterator();
        System.out.println(int.class == Integer.class);
        int num1 = 1;
        Integer num2 = 1;
        System.out.println(Integer.TYPE);
        System.out.println(A.class);
        System.out.println(Integer[].class.getName());
        Class<B> bClass = B.class;
        Field[] declaredFields = bClass.getDeclaredFields();
        System.out.println(Arrays.toString(declaredFields));
        Method[] declaredMethods = bClass.getDeclaredMethods();
        System.out.println(Arrays.toString(declaredMethods));
        A a1 = new A();
        Method aClassMethod = aClass.getMethod("af", int.class, String.class);
        Object invoke = aClassMethod.invoke(a1, 1000, "args");
        System.out.println(invoke);
        System.out.println(Integer.class.getMethod("parseInt", String.class).invoke(null, "68"));
        A a2 = A.class.newInstance();
        System.out.println(a2);
        Constructor<A> constructor = A.class.getConstructor(int.class);
        A a3 = constructor.newInstance(1000000);
        System.out.println(a3);
        Consumer<MyFunction> consumer1 = MyFunction::test;
        C c = (s5) -> 1;
        Consumer<Integer> newConsumer = int[]::new;
        System.out.println(Direction.NORTH.getName());
        Direction.NORTH.setName("northDirection");
        System.out.println(Direction.NORTH.getName());
        System.out.println(newList);
        System.out.println(newList.stream().reduce(0, (i1, i2) -> i1 + i2));
        System.out.println("Hello IDEA!");
    }

    static void addElement(ArrayList arr) {
        arr.add(2);
        arr = new ArrayList();
        arr.add(3);
        arr.add(4);
    }

    static void addElement(int[] arr) {
        arr[1] = 68;
        arr = new int[5];
    }

    @SafeVarargs
    public static <T> void display(T... array) {
        for (T t : array) {
            System.out.println(t);
        }
    }

}

enum Direction {
    NORTH("north"), SOUTH("south"), EAST("east"), WEST("west");

    private String name;

    Direction(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void a() {
        System.out.println("Direction.a()");
    }
}

interface C {
    int cMethod(String s);
}

interface MyFunction {
    void test();
}

interface Finder {
    int find(String s1, String s2);

    static void print() {
        System.out.println("Finder interface");
    }
}

@Range(max = Integer.MAX_VALUE)
class A {
    public int a = 10;
    public int b = 100;
    public int c = 1000;
    @Range
    private int x = 10000;

    A() {
    }

    public A(int a) {
        this.a = a;
        System.out.println("A(int a) constructor");
    }

    public int getX() {
        return x;
    }

    public int af(int x, String s) {
        System.out.println("af()" + x + s);
        return 1000;
    }

    @Override
    public String toString() {
        return "A{" +
                "a=" + a +
                ", b=" + b +
                ", c=" + c +
                ", x=" + x +
                '}';
    }

    public void af() {
    }
}

class B extends A {
    int a = 20;
    public int bVal = 100;

    B(int a) {
        System.out.println("B(int a) constructor");
    }

    @Override
    public void af() {
        System.out.println("bf()");
    }
}


class MyThread implements Runnable {

    int money = 100;
    Lock lock = new ReentrantLock();

    public void use() {
        lock.lock();
        try {
            System.out.println(Thread.currentThread().getName() + " ------> " + money--);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void run() {
        while (money > 0) {
            use();
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

}

class MyCallable implements Callable<Integer> {

    @Override
    public Integer call() throws Exception {
        int sum = 0;
        for (int i = 1; i <= 100; i++) {
            sum += i;
        }
        return sum;
    }

}

@Retention(RetentionPolicy.RUNTIME)
@interface Range {
    int value() default 0;

    int min() default 0;

    int max() default 1024;
}

class Animal {

}

interface MyAnimal1 {
    int a = 1;
}

interface MyAnimal2 {
    int a = 1;
}

class Cat extends Animal implements MyAnimal1, MyAnimal2 {

}

class Complex implements Comparable<Complex> {
    int real;
    double image;

    public Complex() {

    }

    @Override
    public int compareTo(Complex o) {
        return this.real - o.real;
    }
}