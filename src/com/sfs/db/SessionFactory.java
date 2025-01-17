package com.sfs.db;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.boot.registry.internal.StandardServiceRegistryImpl;
import org.hibernate.cfg.Configuration;

import com.sfs.util.Constants;
import com.sfs.util.Util;

/**
 * Configures and provides access to Hibernate sessions, tied to the
 * current thread of execution.  Follows the Thread Local Session
 * pattern, see {@link http://hibernate.org/42.html }.
 */
public class SessionFactory {
    /**
     * Location of hibernate.cfg.xml file.
     * Location should be on the classpath as Hibernate uses
     * #resourceAsStream style lookup for its configuration file.
     * The default classpath location of the hibernate config file is
     * in the default package. Use #setConfigFile() to update
     * the location of the configuration file for the current session.
     */
    private static String CONFIG_FILE = "config/hibernate.cfg.xml";
    private static String LOCAL_CONFIG_FILE = "local/hibernateLocal.cfg.xml";
    private static ThreadLocal<Session> threadLocal;
    private static Configuration configuration;
    private static org.hibernate.SessionFactory sessionFactory;
    private static StandardServiceRegistryImpl serviceRegistry;

    private SessionFactory() {
    }

    public static void initialize() {
        try {
            Util.logInfo("Start initialize");
            threadLocal = new ThreadLocal<Session>();

            configuration = new Configuration().configure(Constants.EXTENSION_PATH + CONFIG_FILE);
            Util.logInfo("Load config " + CONFIG_FILE + " complete");

            final StandardServiceRegistryBuilder serviceRegistryBuilder = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties());
            serviceRegistry = (StandardServiceRegistryImpl) serviceRegistryBuilder.build();
            sessionFactory = configuration.buildSessionFactory(serviceRegistry);
            Util.logInfo("BuildSessionFactory complete");
        } catch (Exception e) {
            Util.logError("[SessionFactory] %%%% Error Creating SessionFactory %%%% " + e.getMessage());
            StackTraceElement[] elements = e.getStackTrace();
            for (int i = 0; i < elements.length; ++i) {
                Util.logError(elements[i].toString());
            }
        }
    }

    public static void initializeLocal() {
        try {
            System.out.println("Start initialize");
            threadLocal = new ThreadLocal<Session>();

            configuration = new Configuration().configure(LOCAL_CONFIG_FILE);
            System.out.println("Load config " + LOCAL_CONFIG_FILE + " complete");

            final StandardServiceRegistryBuilder serviceRegistryBuilder = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties());
            serviceRegistry = (StandardServiceRegistryImpl) serviceRegistryBuilder.build();
            sessionFactory = configuration.buildSessionFactory(serviceRegistry);
            System.out.println("BuildSessionFactory complete");
        } catch (Exception e) {
            System.out.println("[SessionFactory] %%%% Error Creating SessionFactory %%%% " + e.getMessage());
            StackTraceElement[] elements = e.getStackTrace();
            for (int i = 0; i < elements.length; ++i) {
                System.out.println(elements[i].toString());
            }
        }
    }

    /**
     * Get hibernate session
     *
     * @return Session
     * @throws HibernateException
     */
    public static Session getSession() throws HibernateException {
        Session session = (Session) threadLocal.get();

        if (session == null || !session.isOpen()) {
            session = (sessionFactory != null) ? sessionFactory.openSession() : null;
            threadLocal.set(session);
        }

        return session;
    }

    /**
     * Close hibernate session
     */
    public static void closeSessionFactory() {
        sessionFactory.close();
        StandardServiceRegistryBuilder.destroy(serviceRegistry);
        Util.logInfo("CloseSessionFactory");
    }
}