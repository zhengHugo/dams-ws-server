package endpoint;

import javax.jws.WebMethod;
import javax.jws.WebService;
import dto.AppointmentType;

@WebService
public interface Admin {

  @WebMethod
  boolean addAppointment(String appointmentId, AppointmentType type, int capacity);

  @WebMethod
  String removeAppointment(String id, AppointmentType type);

  @WebMethod
  String listAppointmentAvailability(AppointmentType type);

  @WebMethod
  String listLocalAppointmentAvailability(AppointmentType  type);

}
