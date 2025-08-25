/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement(name = "bestiarum")
@XmlAccessorType(XmlAccessType.FIELD)
@JsonRootName(value = "bestiarum")
public class Bestiarum {
    @XmlElement(name = "monster")
    @JsonProperty("monster")
    private List<Monster> monsters = new ArrayList<>();

    public List<Monster> getMonsters() { return monsters; }
    public void setMonsters(List<Monster> monsters) { this.monsters = monsters; }
}
