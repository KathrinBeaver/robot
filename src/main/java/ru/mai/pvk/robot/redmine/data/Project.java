/*
 * To change this license header, choose License Headers in Project RedmineConnectionProperties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.mai.pvk.robot.redmine.data;

import lombok.Getter;

/**
 *
 * @author user
 */
@Getter
public class Project {

    private String id;
    private String ProjectName;
    
    public Project(String name, String id) {
        this.id = id;
        this.ProjectName = name;
    }

    public void setId(String id) {
        this.id = id;
    }

    /**
     * @param ProjectName the ProjectName to set
     */
    public void setProjectName(String ProjectName) {
        this.ProjectName = ProjectName;
    }
    public String toString(){
        return this.getProjectName() + "-" + this.id;
    }
}
