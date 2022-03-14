package database;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Optional;
import dto.AppointmentType;
import model.appointment.Appointment;
import model.appointment.AppointmentId;
import model.PatientId;

public class Database {

  private final HashMap<AppointmentType, HashMap<AppointmentId, Appointment>> hashMap;
  private static Database instance;

  private Database() {
    hashMap = new HashMap<>();
  }

  public static synchronized Database getInstance() {
    if (instance == null) {
      instance = new Database();
    }
    return instance;
  }

  public synchronized void add(AppointmentId id, AppointmentType type, int capacity) {
    if (!hashMap.containsKey(type)) {
      hashMap.put(type, new HashMap<>());
    }
    hashMap.get(type).put(id, new Appointment(id, capacity, type));
  }

  public synchronized boolean update(Appointment appointment, AppointmentType appointmentType) {
    HashMap<AppointmentId, Appointment> innerHashmap = hashMap.get(appointmentType);
    if (innerHashmap != null) {
      AppointmentId id = appointment.getAppointmentId();
      return innerHashmap.replace(id, appointment) != null;
    }
    return false;
  }

  public synchronized void remove(AppointmentId id, AppointmentType type) {
    HashMap<AppointmentId, Appointment> innerHashMap = hashMap.get(type);
    if (innerHashMap != null) {
      Appointment appointment = innerHashMap.get(id);
      if (appointment != null) {
        innerHashMap.remove(id);
      }
    }
  }

  public Optional<Appointment> findByTypeAndId(AppointmentType type, AppointmentId id) {
    HashMap<AppointmentId, Appointment> innerHashMap = hashMap.get(type);
    if (innerHashMap != null) {
      Appointment appointment = innerHashMap.get(id);
      if (appointment != null) {
        return Optional.of(appointment);
      } else {
        return Optional.empty();
      }
    } else {
      return Optional.empty();
    }
  }

  public List<Appointment> findAllByPatientId(PatientId pid) {
    List<Appointment> appointments = new ArrayList<>();
    for (HashMap<AppointmentId, Appointment> innerHashMap : hashMap.values()) {
      for (Appointment appointment : innerHashMap.values()) {
        if (appointment.getPatientIds().contains(pid)) {
          appointments.add(appointment);
        }
      }
    }
    return appointments;
  }

  public Collection<Appointment> findAllByType(AppointmentType type) {
    HashMap<AppointmentId, Appointment> innerHashMap = hashMap.get(type);
    if (innerHashMap != null) {
      return innerHashMap.values();
    } else {
      return new ArrayList<>();
    }
  }

  public Optional<AppointmentId> findNextAppointmentId(AppointmentType type, AppointmentId thisId) {
    // 1. Get the inner hashmap that contain this appointment
    // 2. Iterator over the inner hashmap and get the next available one
    HashMap<AppointmentId, Appointment> innerHashMap = hashMap.get(type);
    if (innerHashMap != null) {
      AppointmentId nextAvailableAppointmentId = null;
      for (Entry<AppointmentId, Appointment> appointmentIdAppointmentEntry :
          innerHashMap.entrySet()) {
        AppointmentId nextId = appointmentIdAppointmentEntry.getKey();
        if (thisId.compareTo(nextId) < 0) {
          if (nextAvailableAppointmentId == null
              || nextId.compareTo(nextAvailableAppointmentId) < 0) {
            nextAvailableAppointmentId = nextId;
          }
        }
      }
      return nextAvailableAppointmentId == null
          ? Optional.empty()
          : Optional.of(nextAvailableAppointmentId);
    }
    return Optional.empty();
  }
}