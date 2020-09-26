package com.thenextstep4u.blogging.bootstrap;

import com.thenextstep4u.blogging.model.BlogPost;
import com.thenextstep4u.blogging.model.BlogPostVariable;
import com.thenextstep4u.blogging.model.Comment;
import com.thenextstep4u.blogging.repositories.BlogPostRepository;
import com.thenextstep4u.blogging.repositories.CommentRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Profile({"dev","prod","test"})
@Component
public class MongoDBBootstrap implements ApplicationListener<ContextRefreshedEvent> {
    private BlogPostRepository blogPostRepository;
    private CommentRepository commentRepository;
    @Value("${thenextstep4u.web.url}")
    private String thenextstep4uWebURL;

    public MongoDBBootstrap(BlogPostRepository blogPostRepository, CommentRepository commentRepository) {
        this.blogPostRepository = blogPostRepository;
        this.commentRepository = commentRepository;
    }
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        initData();
    }

    private void initData() {

        BlogPost blogPost = blogPostRepository.findByTitle("A Rocky Start");
        if (blogPost == null) {
            blogPost = new BlogPost();
            blogPost.setTitle("A Rocky Start");
            blogPost.setHtmlTemplate("blog/arockystart.html");
            blogPost.setFirstParagraph("When I started my career way back in 1999 after getting my B.Sc Information Technology degree, I realized that " +
                    "I didn't really know as much as I thought I did ...");
            blogPost.setDatePosted(LocalDate.now());
            blogPost.setCoverImageURL("/images/arockystart.png");
            blogPost.setUrlPathName("arockystart");
            blogPostRepository.save(blogPost);
//            BlogPostVariable blogPostVariable1 = new BlogPostVariable("linkURL",thenextstep4uWebURL + "/api/v1/blog/" + blogPost.getId());
//            blogPost.getBlogPostVariables().add(blogPostVariable1);
//            blogPostRepository.save(blogPost);
        }

        BlogPost blogPost1 = blogPostRepository.findByTitle("8 Traits to Successful Software Development");
        if (blogPost1 == null) {
            blogPost1 = new BlogPost();
            blogPost1.setTitle("8 Traits to Successful Software Development");
            blogPost1.setHtmlTemplate("blog/traitsfordeveloper.html");
            blogPost1.setFirstParagraph("There are some personality/character traits that most developers have acquired, " +
                    "that makes them successful. You aren't always born with these traits and can develop or work on it to " +
                    "become better at it. Here is the list of traits for you...");
            blogPost1.setDatePosted(LocalDate.now());
            blogPost1.setCoverImageURL("/images/traits-for-dev.jpg");
            blogPost1.setUrlPathName("traitsToDevSuccess");
            blogPostRepository.save(blogPost1);
        }

        BlogPost blogPost2 = blogPostRepository.findByTitle("Learn to do by doing");
        if (blogPost2 == null) {
            blogPost2 = new BlogPost();
            blogPost2.setTitle("Learn to do by doing");
            blogPost2.setHtmlTemplate("blog/learnbydoing.html");
            blogPost2.setFirstParagraph(" When learning something new in software development, don't make the mistake of just reading through the material and thinking " +
                    "that you understand it. ");
            blogPost2.setDatePosted(LocalDate.now());
            blogPost2.setCoverImageURL("/images/learnbydoing.png");
            blogPost2.setUrlPathName("learnbydoing");
            blogPostRepository.save(blogPost2);
        }

        BlogPost blogPost3 = blogPostRepository.findByTitle("Why Java?");
        if (blogPost3 == null) {
            blogPost3 = new BlogPost();
            blogPost3.setTitle("Why Java?");
            blogPost3.setHtmlTemplate("blog/whyjava.html");
            blogPost3.setFirstParagraph("There are so many programming languages to choose from when deciding which one to learn.  It can be quite " +
                    " overwhelming to decide on the right one. ");
            blogPost3.setDatePosted(LocalDate.now());
            blogPost3.setCoverImageURL("/images/whyjava.png");
            blogPost3.setUrlPathName("whyjava");
            blogPostRepository.save(blogPost3);
        }

        BlogPost blogPost4 = blogPostRepository.findByTitle("Plan Your Code, Code Your Plan");
        if (blogPost4 == null) {
            blogPost4 = new BlogPost();
            blogPost4.setTitle("Plan Your Code, Code Your Plan");
            blogPost4.setHtmlTemplate("blog/planyourcode.html");
            blogPost4.setFirstParagraph("Software design is an important part of software development.  Designing your software properly before you start " +
                    "writing your code will save time and effort down the line.");
            blogPost4.setDatePosted(LocalDate.now());
            blogPost4.setCoverImageURL("/images/planyourcode.png");
            blogPost4.setUrlPathName("planyourcode");
            blogPostRepository.save(blogPost4);
        }

        BlogPost blogPost5 = blogPostRepository.findByTitle("Why HTML, CSS and Bootstrap??");
        if (blogPost5 == null) {
            blogPost5 = new BlogPost();
            blogPost5.setTitle("Why HTML, CSS and Bootstrap??");
            blogPost5.setHtmlTemplate("blog/whyhtml.html");
            blogPost5.setFirstParagraph("With everything today moving towards the internet, it is always a good idea as " +
                    "a software developer to know how to create webp ages and make them look good ...");
            blogPost5.setDatePosted(LocalDate.now());
            blogPost5.setCoverImageURL("/images/whyhtml.png");
            blogPost5.setUrlPathName("whyhtml");
            blogPostRepository.save(blogPost5);
        }

        BlogPost blogPost6 = blogPostRepository.findByTitle("What is Spring Framework?");
        if (blogPost6 == null) {
            blogPost6 = new BlogPost();
            blogPost6.setTitle("What is Spring Framework?");
            blogPost6.setHtmlTemplate("blog/springframework.html");
            blogPost6.setFirstParagraph("If you want to create Enterprise Java applications or Java Web applications, " +
                    "Spring Framework with Spring boot is a good option to look at ...");
            blogPost6.setDatePosted(LocalDate.now());
            blogPost6.setCoverImageURL("/images/springframework.png");
            blogPost6.setUrlPathName("springframework");
            blogPostRepository.save(blogPost6);
        }

        BlogPost blogPost7 = blogPostRepository.findByTitle("What is Object-oriented programming?");
        if (blogPost7 == null) {
            blogPost7 = new BlogPost();
            blogPost7.setTitle("What is Object-oriented programming?");
            blogPost7.setHtmlTemplate("blog/oop.html");
            blogPost7.setFirstParagraph("Knowing the basics of Object-Oriented programming will help you a lot when " +
                    "learning how to code in an OOP language.  I'll explain to you in simple terms how an OOP language works.");
            blogPost7.setDatePosted(LocalDate.now());
            blogPost7.setCoverImageURL("/images/oop.png");
            blogPost7.setUrlPathName("oop");
            blogPostRepository.save(blogPost7);
        }

        BlogPost blogPost8 = blogPostRepository.findByTitle("The 4 Principles of Object-oriented programming - Part 1");
        if (blogPost8 == null) {
            blogPost8 = new BlogPost();
            blogPost8.setTitle("The 4 Principles of Object-oriented programming - Part 1");
            blogPost8.setHtmlTemplate("blog/4principles1.html");
            blogPost8.setFirstParagraph("These 4 principles are an important part of object-oriented programming. " +
                    "If you want to be successful in OOP, you need to know and understand these principles.");
            blogPost8.setDatePosted(LocalDate.now());
            blogPost8.setCoverImageURL("/images/4principles1.png");
            blogPost8.setUrlPathName("4principles1");
            blogPostRepository.save(blogPost8);
        }

        BlogPost blogPost9 = blogPostRepository.findByTitle("The 4 Principles of Object-oriented programming - Part 2");
        if (blogPost9 == null) {
            blogPost9 = new BlogPost();
            blogPost9.setTitle("The 4 Principles of Object-oriented programming - Part 2");
            blogPost9.setHtmlTemplate("blog/4principles2.html");
            blogPost9.setFirstParagraph("There are 4 principles of oop programming that you need to master.  " +
                    "Today I'll be discussing the next principles with you in detail, namely Inheritance and Polymorphism.");
            blogPost9.setDatePosted(LocalDate.now());
            blogPost9.setCoverImageURL("/images/4principles2.png");
            blogPost9.setUrlPathName("4principles2");
            blogPostRepository.save(blogPost9);
        }

    }
}
