package org.kathrynhuxtable.heroes.createdb;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import org.kathrynhuxtable.heroes.service.persistence.HeroDAO;
import org.kathrynhuxtable.heroes.service.persistence.LoginInfoDAO;
import org.kathrynhuxtable.heroes.service.persistence.PrivilegeDAO;
import org.kathrynhuxtable.heroes.service.persistence.UserDAO;
import org.kathrynhuxtable.heroes.service.persistence.domain.HeroDO;
import org.kathrynhuxtable.heroes.service.persistence.domain.LoginInfoDO;
import org.kathrynhuxtable.heroes.service.persistence.domain.PrivilegeDO;
import org.kathrynhuxtable.heroes.service.persistence.domain.UserDO;

@Slf4j
@Component
public class InitializeData {

    private final InitialData initialData;
    private final LoginInfoDAO loginInfoDao;
    private final PrivilegeDAO privilegeDao;
    private final UserDAO userDao;
    private final HeroDAO heroDao;

    public InitializeData(InitialData initialData,
                          LoginInfoDAO loginInfoDao,
                          PrivilegeDAO privilegeDao,
                          UserDAO userDao,
                          HeroDAO heroDao) {
        this.initialData = initialData;
        this.loginInfoDao = loginInfoDao;
        this.privilegeDao = privilegeDao;
        this.userDao = userDao;
        this.heroDao = heroDao;
    }

    public void initialize() {
        log.info("Initializing Users");
        initializeUsers();
        log.info("Initialized Users");

        log.info("Initializing Heroes");
        initializeHeroes();
        log.info("Initialized Heroes");
    }

    private void initializeUsers() {
        loginInfoDao.deleteAll();
        privilegeDao.deleteAll();
        userDao.deleteAll();

        for (InitialData.UserData userData : initialData.getUsers()) {
            UserDO user = new UserDO();
            user.setLastName(userData.getLastName());
            user.setFirstName(userData.getFirstName());
            user.setPreferredTheme(userData.getPreferredTheme());
            user = userDao.save(user);

            if (userData.getPrivileges() != null) {
                for (String privilege : userData.getPrivileges()) {
                    PrivilegeDO privilegeDO = PrivilegeDO.builder()
                            .userId(user.getUserId())
                            .privilege(privilege)
                            .build();
                    user.getPrivileges().add(privilegeDO);
                }
                user = userDao.save(user);
            }

            LoginInfoDO loginInfo = LoginInfoDO.builder()
                    .username(userData.getUsername())
                    .password(userData.getPassword())
                    .userId(user.getUserId())
                    .build();
            loginInfoDao.save(loginInfo);
        }
    }

    private void initializeHeroes() {
        heroDao.deleteAll();

        for (InitialData.HeroData heroData : initialData.getHeroes()) {
            HeroDO hero = HeroDO.builder()
                    .name(heroData.getName())
                    .alterEgo(heroData.getAlterEgo())
                    .power(heroData.getPower())
                    .rating(heroData.getRating())
                    .build();
            heroDao.save(hero);
        }
    }
}
