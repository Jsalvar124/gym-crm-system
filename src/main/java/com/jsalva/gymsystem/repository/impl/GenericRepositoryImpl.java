package com.jsalva.gymsystem.repository.impl;
import com.jsalva.gymsystem.repository.GenericRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public abstract class GenericRepositoryImpl<T, K> implements GenericRepository<T,K> {

    @PersistenceContext  // Add persistence context to entity manager
    private EntityManager em;

    private final Class<T> entityClass;

    private final Logger logger = LoggerFactory.getLogger(GenericRepositoryImpl.class);


    public GenericRepositoryImpl(Class<T> entityClass) { // removed em constructor injection, annotation handles it.
        this.entityClass = entityClass;
    }

    public T create(T t) {
//        EntityTransaction tx = em.getTransaction(); // Remove Transaction
        try{
//            tx.begin(); // remove Begin
            em.persist(t);
//            tx.commit(); // remove Commit
            return t;
        }catch (RuntimeException e){
//            if(tx.isActive()){
//                tx.rollback(); // remove rollback!
//            }
            logger.error("Failed to create entity: {}", e.getMessage());
            throw e;
        }
    }

    public T update(T t) {
        try{
            T merged = em.merge(t);
            return merged;
        }catch (RuntimeException e){
            logger.error("Failed to update entity: {}", e.getMessage());
            throw e;
        }
    }

    public boolean delete(K id) {
        try {
            T entity = em.find(entityClass, id);
            if (entity != null) {
                em.remove(entity);
                logger.info("Entity removed successfully");
                return true;
            } else {
                logger.warn("Failed to delete entity, Entity not found!");
                return false;
            }
        } catch (RuntimeException e) {

            logger.error("Failed to delete entity: {}", e.getMessage());
            return false;
        }
    }
    public Optional<T> findById(K id) {
        try {
            T entity = em.find(entityClass, id);
            return Optional.ofNullable(entity);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<T> findAll() {
        try {
            String jpql = "SELECT e FROM " + entityClass.getSimpleName() + " e";
            return em.createQuery(jpql, entityClass).getResultList();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

