package com.jsalva.gymsystem.repository.impl;
import com.jsalva.gymsystem.repository.GenericRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public abstract class GenericRepositoryImpl<T, K> implements GenericRepository<T,K> {

    protected EntityManager em;

    private final Class<T> entityClass;


    public GenericRepositoryImpl(Class<T> entityClass, EntityManager em) {
        this.entityClass = entityClass;
        this.em = em;
    }

    @Transactional
    public T create(T t) {
        EntityTransaction tx = em.getTransaction();
        try{
            tx.begin();
            em.persist(t);
            tx.commit();
            return t;
        }catch (RuntimeException e){
            if(tx.isActive()){
                tx.rollback();
            }
            System.err.println("Failed to create entity: " + e.getMessage());
            throw e;
        }
    }

    @Transactional
    public T update(T t) {
        EntityTransaction tx = em.getTransaction();
        try{
            tx.begin();
            T merged = em.merge(t);
            tx.commit();
            return merged;
        }catch (RuntimeException e){
            if(tx.isActive()){
                tx.rollback();
            }
            System.err.println("Failed to update entity: " + e.getMessage());
            throw e;
        }
    }

    @Transactional
    public boolean delete(K id) {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            T entity = em.find(entityClass, id);
            if (entity != null) {
                em.remove(entity);
                tx.commit();
                System.out.println("Entity removed successfully");
                return true;
            } else {
                tx.rollback();
                System.err.println("Failed to delete entity, Entity not found!");
                return false;
            }
        } catch (RuntimeException e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            System.err.println("Failed to delete entity: " + e.getMessage());
            return false;
        }
    }
    @Transactional(readOnly = true)
    public Optional<T> findById(K id) {
        try {
            T entity = em.find(entityClass, id);
            return Optional.ofNullable(entity);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional(readOnly = true)
    public List<T> findAll() {
        try {
            String jpql = "SELECT e FROM " + entityClass.getSimpleName() + " e";
            return em.createQuery(jpql, entityClass).getResultList();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

