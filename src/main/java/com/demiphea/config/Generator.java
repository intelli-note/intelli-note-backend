package com.demiphea.config;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;

import java.util.Collections;

/**
 * Generator
 *
 * @author demiphea
 * @since 17.0.9
 */
public class Generator {
    public static final String DIR = "C:\\Users\\demiphea\\Projects\\JavaProjects\\intelli-note\\intelli-note-backend\\src\\main\\java";
    public static final String XML_DIR = "C:\\Users\\demiphea\\Projects\\JavaProjects\\intelli-note\\intelli-note-backend\\src\\main\\resources\\mapper\\";

    public static void main(String[] args) {
        FastAutoGenerator.create(
                "jdbc:mysql://localhost:3306/intelli_note?useSSL=false&allowPublicKeyRetrieval=true",
                "root",
                "123456"
        ).globalConfig(builder -> {
            builder.outputDir(DIR)
                    .author("demiphea");
        }).packageConfig(builder -> {
            builder.parent("com.demiphea")
                    .mapper("dao")
                    .pathInfo(Collections.singletonMap(OutputFile.xml, XML_DIR));
        }).strategyConfig(builder -> {
            builder.addTablePrefix("tb_");
            builder.entityBuilder()
                    .disableSerialVersionUID()
                    .enableLombok()
                    .enableRemoveIsPrefix()
                    .enableTableFieldAnnotation()
//                    .logicDeleteColumnName("is_deleted")
                    .idType(IdType.ASSIGN_ID);
            builder.mapperBuilder()
                    .formatMapperFileName("%sDao");
        }).templateConfig(builder -> {
            builder.entity("templates/entity.java").mapper("templates/dao.java");
        }).execute();
    }
}
