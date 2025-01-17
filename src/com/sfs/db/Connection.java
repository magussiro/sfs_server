package com.sfs.db;

import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import com.sfs.util.Util;

/**
 * Handler any SQL command or any hibernate object operator
 * 透過Hibernate與DB溝通的工具，可以輸入搜尋條件取得Hibernate物件，也可對Hibernate物件進行更新修改或刪除
 */
public final class Connection {
    public static final boolean init(final String tableName) {
        Session session = getSession();
        try {
            // 執行第一次SQL，確保連線成功
            SQLQuery sqlQuery = session.createSQLQuery("SELECT count(1) FROM " + tableName + ";");
//            sqlQuery.executeUpdate();
            sqlQuery.list();
        } catch (HibernateException e) {
            Util.logError("[Connection] %%%% Error Creating SQLQuery %%%%");
            Util.logException(e);
            return false;
        } finally {
            session.close();
        }
        Util.logInfo("Connection initialize complete");
        
        return true;
    }
    public static final boolean initLocal() {

        Session session = getSession();
        try {
            SQLQuery sqlQuery = session.createSQLQuery("SELECT count(1) FROM game_member_list;");
//            sqlQuery.executeUpdate();
            sqlQuery.list();
        } catch (HibernateException e) {
            System.out.println("[Connection] %%%% Error Creating SQLQuery %%%%");
            System.out.println(e);
            return false;
        } finally {
            session.close();
        }
        System.out.println("Connection initialize complete");

        return true;
    }

    private static Session getSession() {
        return SessionFactory.getSession();
    }
    
    /**
     * Insert or update hibernate object to DB
     * @param object
     * @return
     */
    public static boolean persist(final Object object) {
        final long startTime = new Date().getTime();
        Session session = getSession();
        if (session == null)
            return false;

        Transaction tx = session.beginTransaction();
        try {
            session.saveOrUpdate(object);
            tx.commit();
        } catch (RuntimeException e) {
            Util.logException(e);

            try {
                tx.rollback();
            } catch (HibernateException e1) {
                Util.logException(e1);
            }

            return false;
        } finally {
            session.close();
        }

        final float time = new Date().getTime() - startTime;
        Util.logDebug("persist object time " + time);

        return true;
    }
    
    /**
     * Insert or update hibernate object list to DB
     * @param objects
     * @return
     */
    public static boolean persist(final Collection<?> objects) {
        final long startTime = new Date().getTime();
        Session session = getSession();
        if (session == null)
            return false;

        Transaction tx = session.beginTransaction();
        try {
            for (Iterator<?> iter = objects.iterator(); iter.hasNext();) {
                final Object obj = iter.next();
                if (obj != null) {
                    session.saveOrUpdate(obj);
                }
            }
            tx.commit();
        } catch (RuntimeException e) {
            Util.logException(e);
            try {
                tx.rollback();
            } catch (HibernateException e1) {
                Util.logException(e1);
            }

            return false;
        } finally {
            session.close();
        }

        final float time = new Date().getTime() - startTime;
        Util.logDebug("persist list " + objects.size() + " time " + time);
        return true;
    }
    
    /**
     * Get hibernate object by class name from DB
     * @param className
     * @return
     */
    @SuppressWarnings("rawtypes")
    public static List<?> getClass(final Class className){
        final Session session =  getSession();
        try {
            final Criteria cr = session.createCriteria(className);
            List<?> lista = cr.list();
            if (lista != null && lista.size() > 0) {
                return lista;
            }
        } catch (HibernateException e) {
            Util.logException(e);
        } finally {
            session.close();
        }
        return null;
    }
    
    /**
     * Get hibernate object by integer key value from DB
     * @param className
     * @param columnName
     * @param id
     * @return
     */
    @SuppressWarnings("rawtypes")
    public static List<?> getClassByKey(final Class className, final String columnName, final int id){
        final Session session =  getSession();
        try {
            final Criteria cr = session.createCriteria(className);
            cr.add(Restrictions.eq(columnName, id));       
            List<?> lista = cr.list();
            if (lista != null && lista.size() > 0) {
                return lista;
            }
        } catch (HibernateException e) {
            Util.logException(e);
        } finally {
            session.close();
        }
        return null;
    }
    
    /**
     * Get hibernate object by string key value from DB
     * @param className
     * @param columnName
     * @param keyValue
     * @return
     */
    @SuppressWarnings("rawtypes")
    public static List<?> getClassByKey(final Class className, final String columnName, final String keyValue){
        final Session session =  getSession();
        try {
            final Criteria cr = session.createCriteria(className);
            cr.add(Restrictions.eq(columnName, keyValue));       
            List<?> lista = cr.list();
            if (lista != null && lista.size() > 0) {
                return lista;
            }
        } catch (HibernateException e) {
            Util.logException(e);
        } finally {
            session.close();
        }
        return null;
    }
    public static <T> T getClassByKeySingle(final Class className, final String columnName, final String keyValue) {
        List<?> _list = getClassByKey(className, columnName, keyValue);
        if (_list == null || _list.isEmpty()) {
            return null;
        }
        return (T) _list.get(0);
    }

    public static <T> T getClassByKeySingle(final Class className, final String columnName, final int keyValue) {
        List<?> _list = getClassByKey(className, columnName, keyValue);
        if (_list == null || _list.isEmpty()) {
            return null;
        }
        return (T) _list.get(0);
    }
    
    @SuppressWarnings("rawtypes")
    public static List<?> getClassListByRange(final Class className, final String columnName, final int startId, final int endId) {
        final Session session =  getSession();
        try {
            final Criteria cr = session.createCriteria(className);
            cr.add(Restrictions.between(columnName, startId, endId));   
            List<?> lista = cr.list();
            if (lista != null && lista.size() > 0) {
                return lista;
            }
        } catch (HibernateException e) {
            Util.logException(e);
        } finally {
            session.close();
        }
        return null;
    }
}
