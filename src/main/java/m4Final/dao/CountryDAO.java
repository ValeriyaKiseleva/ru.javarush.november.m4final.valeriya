package m4Final.dao;

import m4Final.entity.Country;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import java.util.List;


public class CountryDAO {
    private final SessionFactory sessionFactory;

    public CountryDAO(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public List<Country> getAll() {
        Query<Country> query = (Query<Country>) sessionFactory.getCurrentSession().createQuery(
                "select c from Country c join fetch c.languages",
                Country.class);
        return query.list();
    }

}
