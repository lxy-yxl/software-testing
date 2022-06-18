package com.example.softwaretest.Service.impl;

import com.example.softwaretest.Entity.Triangle;
import com.example.softwaretest.Mapper.TriangleMapper;
import com.example.softwaretest.Service.TriangleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TriangleServiceImpl implements TriangleService {

    @Autowired
    private TriangleMapper triangleMapper;

    @Override
    public List<Triangle> getTestCases() {
        return triangleMapper.getTestCases();
    }

    @Override
    public List<Integer> testTriangle(List<Triangle> testCases) {
        return null;
    }

    @Override
    public void cleanTestCases() {
        triangleMapper.cleanTestCases();
    }

    @Override
    public void saveTestCases(Triangle triangle) {
        triangleMapper.saveTestCases(triangle);
    }

    @Override
    public String testTriangle(Triangle triangle){
            int a = triangle.getA();
            int b = triangle.getB();
            int c = triangle.getC();
            if(a<1||a>200){
                return "error!a要为[1,200]的数";
            }else if(b<1||b>200) {
                return "error!b要为[1,200]的数";
            }else if(c<1||c>200){
                return "error!c要为[1,200]的数";
            }
            if (a+b>c&&a+c>b&&b+c>a) {
                if (a==b&&b==c) {
                    return "等边三角形";
                }else if ((a==b&&b!=c)||(b==c&&b!=a)||(a==c&&a!=b)) {
                    return "等腰三角形";
                }else{
                    return "一般三角形";
                }
            }else {
                return "非三角形";
            }
        }


}
