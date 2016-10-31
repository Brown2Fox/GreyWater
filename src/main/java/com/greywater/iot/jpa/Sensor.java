package com.greywater.iot.jpa;

import com.greywater.iot.persistence.PersistManager;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/*Таблица датчиков. Поля говорят сами за себя
* Каждый датчик в системе будет иметь запись в таблице*/
@Entity
@Table(name = "SENSORS_TABLE")
@NamedQueries({
        @NamedQuery(name = "Sensor.getAll", query = "SELECT s from Sensor s"),
        @NamedQuery(name = "Sensor.getByID", query = "SELECT s from Sensor s where s.id = :id")
})
@XmlRootElement
public class Sensor {

    // === FIELDS === //
    @Id
    @Column(name = "SENSOR_ID")
    private String id = UUID.randomUUID().toString();

    @Column(name = "TYPE")
    private String type = null;

    @ManyToMany(mappedBy = "sensors", fetch = FetchType.EAGER)
    private List<VirtualSensor> virtualSensors = new ArrayList<>();


    // === GETTERS AND SETTERS === //
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public List<VirtualSensor> getVirtualSensors() {
        return virtualSensors;
    }
    public void addVirtualSensor(VirtualSensor vs) {
        if (!virtualSensors.contains(vs)) {
            virtualSensors.add(vs);
        }
    }

    // === TRANSIENT === //
    private transient Message actualMessage = null;

    public Message getActualMessage() {
        return actualMessage;
    }
    public void updateActualMessage(Message actualMessage) {
        this.actualMessage = actualMessage;
    }

    public static List<Sensor> getAll() {
        EntityManager entityManager = PersistManager.newEntityManager();
        List<Sensor> sensors = entityManager.createNamedQuery("Sensor.getAll", Sensor.class).getResultList();
        entityManager.close();
        return sensors;
    }

}
