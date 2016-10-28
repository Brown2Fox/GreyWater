package com.greywater.iot.jpa;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/*Таблица датчиков. Поля говорят сами за себя
* Каждый датчик в системе будет иметь запись в таблице*/
@Entity
@Table(name = "RAW_SENSORS_TABLE")
@NamedQueries({
        @NamedQuery(name = "Sensor.getAll", query = "SELECT s from Sensor s"),
        @NamedQuery(name = "Sensor.getByID", query = "SELECT s from Sensor s where s.id = :id")
})
@XmlRootElement
public class Sensor {

    // === FIELDS === //
    @Id
    @Column(name = "ID")
    @GeneratedValue
    private Long id;

    @Column(name = "TYPE")
    private String type = null;

    @ManyToMany(mappedBy = "sensors")
    private List<VirtualSensor> virtualSensors;


    // === GETTERS AND SETTERS === //
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }

    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }

    public Message getActualMessage() {
        return actualMessage;
    }
    public void updateActualMessage(Message actualMessage) {
        this.actualMessage = actualMessage;
    }

    public List<VirtualSensor> getVirtualSensors() {
        return virtualSensors;
    }


    // === TRANSIENT === //
    private transient Message actualMessage;

    public static List<Sensor> getAll() {
        EntityManager entityManager = Persistence.createEntityManagerFactory("GreyWater").createEntityManager();
        List<Sensor> sensors = entityManager.createNamedQuery("Sensor.getAll", Sensor.class).getResultList();
        entityManager.close();
        return sensors;
    }

}
