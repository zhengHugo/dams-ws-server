import common.GlobalConstants;
import impl.AdminImpl;
import impl.PatientImpl;
import javax.xml.ws.Endpoint;
import model.City;

public class ServicePublisher {

  public static void main(String[] args) {

    switch (args[0]) {
      case "mtl":
        GlobalConstants.thisCity = City.Montreal;
        Endpoint.publish("http://localhost:8080/adminservice", new AdminImpl());
        Endpoint.publish("http://localhost:8080/patientservice", new PatientImpl());
        break;
      case "que":
        GlobalConstants.thisCity = City.Quebec;
        Endpoint.publish("http://localhost:8081/adminservice", new AdminImpl());
        Endpoint.publish("http://localhost:8081/patientservice", new PatientImpl());
        break;
      case "she":
        GlobalConstants.thisCity = City.Sherbrooke;
        Endpoint.publish("http://localhost:8082/adminservice", new AdminImpl());
        Endpoint.publish("http://localhost:8082/patientservice", new PatientImpl());
        break;
      default:
        System.out.println("Invalid arg...");
    }

  }
}