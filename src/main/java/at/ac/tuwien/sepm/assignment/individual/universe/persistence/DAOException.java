package at.ac.tuwien.sepm.assignment.individual.universe.persistence;

import java.sql.SQLException;

public class DAOException extends SQLException {

    public DAOException(String msg){
        super(msg);
    }
}
