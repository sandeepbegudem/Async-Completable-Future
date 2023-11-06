import com.fasterxml.jackson.core.type.TypeReference;import com.fasterxml.jackson.databind.DeserializationFeature;import com.fasterxml.jackson.databind.MapperFeature;import com.fasterxml.jackson.databind.ObjectMapper;import dto.Employees;import java.io.File;import java.io.IOException;import java.util.List;import java.util.concurrent.CompletableFuture;import java.util.concurrent.ExecutionException;import java.util.concurrent.Executor;import java.util.concurrent.Executors;import java.util.stream.Collectors;public class RunAsyncDemo {    public Void saveEmployees(File jsonFile) throws ExecutionException, InterruptedException {        ObjectMapper objectMapper = new ObjectMapper();       //  Executor executor = Executors.newFixedThreadPool(10);                CompletableFuture<Void> runAsyncRunnable = CompletableFuture.runAsync(new Runnable() {            @Override            public void run() {                try {                    objectMapper.disable(DeserializationFeature                            .FAIL_ON_UNKNOWN_PROPERTIES);                    List<Employees> employees = objectMapper                            .readValue(jsonFile, new TypeReference<List<Employees>>() {                            });                    // here we can use the repository.saveAll() method using spring boot                    System.out.println("Thread: " + Thread.currentThread().getName());                    employees.stream().forEachOrdered(System.out::println);                    System.out.println(employees.size());                } catch (IOException e) {                    throw new RuntimeException(e);                }            }        });        return runAsyncRunnable.get();    }    public Void saveEmployeesUsingLambda(File jsonFile) throws ExecutionException, InterruptedException {        ObjectMapper objectMapper = new ObjectMapper();        objectMapper.disable(DeserializationFeature              .FAIL_ON_UNKNOWN_PROPERTIES);        Executor executor = Executors.newFixedThreadPool(10);        CompletableFuture<Void> runAsyncRunnable = CompletableFuture.runAsync(()-> {                try {                    List<Employees> employees = objectMapper                            .readValue(jsonFile, new TypeReference<List<Employees>>() {                            });                    // here we can use the repository.saveAll() method using spring boot                    System.out.println("Thread: " + Thread.currentThread().getName());                    List<Employees> collect =                            employees.stream().collect(Collectors.toList());                    collect.stream().forEach(System.out::println);                    System.out.println(employees.size());                } catch (IOException e) {                    throw new RuntimeException(e);                }        }, executor);        return runAsyncRunnable.get();    }}