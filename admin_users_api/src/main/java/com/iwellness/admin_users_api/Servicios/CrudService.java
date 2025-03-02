package com.iwellness.admin_users_api.Servicios;

public interface CrudService<T, ID> {
    T save(T entity);

    T findById(ID id);

    void deleteById(ID id);
}
