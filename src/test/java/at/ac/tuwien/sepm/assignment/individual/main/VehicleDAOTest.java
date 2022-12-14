package at.ac.tuwien.sepm.assignment.individual.main;

import at.ac.tuwien.sepm.assignment.individual.booking.entities.SearchFilter;
import at.ac.tuwien.sepm.assignment.individual.booking.entities.Vehicle;
import at.ac.tuwien.sepm.assignment.individual.booking.exception.DAOException;
import at.ac.tuwien.sepm.assignment.individual.booking.persistence.VehicleDAO;
import at.ac.tuwien.sepm.assignment.individual.booking.persistence.VehicleDAOImp;
import at.ac.tuwien.sepm.assignment.individual.booking.util.DBUtil;
import org.junit.Test;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class VehicleDAOTest {

    VehicleDAO vehicleDAO = new VehicleDAOImp(DBUtil.getConnection());
    public VehicleDAOTest() throws SQLException{
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateVehicleExxception() throws DAOException {
        //Assert.assertThat(universeService.calculateAnswer(), is("42!"));
        vehicleDAO.create(null);
    }

    @Test
    public void testGetAllVehicle() throws DAOException {
        Vehicle vehicle = new Vehicle();
        vehicle.setModel("car44");
        vehicle.setConstructionYear(1999);
        vehicle.setSeating(2);
        vehicle.setPlateNumber("dsfgsg32");
        vehicle.setPowerUnit("Brawn");
        vehicle.setPower(11);
        vehicle.setBasePrice(11);
        vehicle.setCreateDate(new Timestamp(System.currentTimeMillis()));

        int vehichleAnzahl = vehicleDAO.getAllVehichle().size();

        vehicleDAO.create(vehicle);
        assertTrue(vehichleAnzahl < vehicleDAO.getAllVehichle().size());
    }

    @Test
    public void testUpdateVehicle() throws DAOException {
        Vehicle vehicle = new Vehicle();
        vehicle.setModel("car44");
        vehicle.setConstructionYear(1999);
        vehicle.setSeating(2);
        vehicle.setPlateNumber("dsfgsg32");
        vehicle.setPowerUnit("Brawn");
        vehicle.setPower(11);
        vehicle.setBasePrice(11);
        vehicle.setCreateDate(new Timestamp(System.currentTimeMillis()));
        vehicle = vehicleDAO.create(vehicle);

        assertTrue(vehicle.getModel().equals("car44"));

        vehicle.setModel("car66");
        Vehicle vehicleUpdated = vehicleDAO.update(vehicle);

        assertTrue(vehicleUpdated.getModel().equals("car66"));

    }

    @Test
    public void testSearchVehicle() throws DAOException {
        Vehicle vehicle = new Vehicle();
        vehicle.setModel("car44");
        vehicle.setConstructionYear(1999);
        vehicle.setSeating(2);
        vehicle.setPlateNumber("dsfgsg32");
        vehicle.setPowerUnit("Brawn");
        vehicle.setPower(11);
        vehicle.setBasePrice(11);
        vehicle.setCreateDate(new Timestamp(System.currentTimeMillis()));
        vehicleDAO.create(vehicle);

        SearchFilter searchFilter = new SearchFilter();
        searchFilter.setModel("car44");

        List<Vehicle> vehicleList = vehicleDAO.search(searchFilter);
        for (Vehicle vehicle1 : vehicleList){
                assertTrue(vehicle1.getModel().equals("car44"));
        }


    }



}
