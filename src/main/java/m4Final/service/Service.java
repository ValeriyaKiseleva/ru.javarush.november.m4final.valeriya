package m4Final.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisStringCommands;
import lombok.Getter;
import m4Final.dao.CityDAO;
import m4Final.dao.CountryDAO;
import m4Final.entity.City;
import m4Final.entity.Country;
import m4Final.entity.CountryLanguage;
import m4Final.redis.CityCountry;
import m4Final.redis.Language;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Objects.nonNull;

@Getter
public class Service {
    private final SessionFactory sessionFactory;
    private final RedisClient redisClient;
    private final ObjectMapper mapper;
    private final CityDAO cityDAO;
    private final CountryDAO countryDAO;

    public Service() {
        sessionFactory = new DBSessionFactory().getSessionFactory();
        cityDAO = new CityDAO(sessionFactory);
        countryDAO = new CountryDAO(sessionFactory);
        redisClient = new RedisClientFactory().getRedisClient();
        mapper = new ObjectMapper();
    }



    private void shutdown() {
        if (nonNull(sessionFactory)) {
            sessionFactory.close();
        }
        if (nonNull(redisClient)){
            redisClient.shutdown();
        }
    }

    public void startService(Service service) {
        List<City> allCities = service.fetchData(service);
        List<CityCountry> preparedData = service.transformData(allCities);
        service.pushToRedis(preparedData);

        service.sessionFactory.getCurrentSession().close();

        new TestSpeed().testRedisVsMySQL(service);

        service.shutdown();

    }

    private List<City> fetchData(Service service) {
        try (Session session = service.sessionFactory.getCurrentSession()) {
            List<City> allCities = new ArrayList<>();
            session.beginTransaction();

            List<Country> countries = service.countryDAO.getAll();

            int totalCount = service.cityDAO.getTotalCount();
            int step = 500;
            for (int i =0 ; i < totalCount; i += step) {
                allCities.addAll(service.cityDAO.getItems(i, step));
            }
            session.getTransaction().commit();

            return allCities;
        }
    }

    private List<CityCountry> transformData(List<City> cities) {
        return cities.stream().map(city -> {
            CityCountry res = new CityCountry();
            res.setId(city.getId());
            res.setName(city.getName());
            res.setPopulation(city.getPopulation());
            res.setDistrict(city.getDistrict());

            Country country = city.getCountry();
            res.setAlternativeCountryCode(country.getCode2());
            res.setContinent(country.getContinent());
            res.setCountryCode(country.getCode());
            res.setCountryName(country.getName());
            res.setCountryPopulation(country.getPopulation());
            res.setCountryRegion(country.getRegion());
            res.setCountrySurfaceArea(country.getSurfaceArea());
            Set<CountryLanguage> countryLanguages = country.getLanguages();
            Set<Language> languages = countryLanguages.stream().map(cl -> {
                Language language = new Language();
                language.setLanguage(cl.getLanguage());
                language.setIsOfficial(cl.getIsOfficial());
                language.setPercentage(cl.getPercentage());
                return language;
            }).collect(Collectors.toSet());
            res.setLanguages(languages);

            return res;
        }).collect(Collectors.toList());
    }

    private void pushToRedis(List<CityCountry> data) {
        try (StatefulRedisConnection<String, String> connection = redisClient.connect()) {
            RedisStringCommands<String, String> sync = connection.sync();
            for (CityCountry cityCountry : data) {
                try {
                    sync.set(String.valueOf(cityCountry.getId()), mapper.writeValueAsString(cityCountry));
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            }

        }
    }

//    private void testRedisData(List<Integer> ids) {
//        try (StatefulRedisConnection<String, String> connection = redisClient.connect()) {
//            RedisStringCommands<String, String> sync = connection.sync();
//            for (Integer id : ids) {
//                String value = sync.get(String.valueOf(id));
//                try {
//                    mapper.readValue(value, CityCountry.class);
//                } catch (JsonProcessingException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    }

}

