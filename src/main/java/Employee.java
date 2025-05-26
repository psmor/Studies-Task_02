public class Employee {
    private String name;
    private Integer age;
    private Post post;

    public Employee(String name, Integer age, Post post) {
        this.name = name;
        this.age = age;
        this.post = post;
    }

    public String getName() {
        return name;
    }

    public Integer getAge() {
        return age;
    }

    public Post getPost() {
        return post;
    }
}
