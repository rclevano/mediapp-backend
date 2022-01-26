package com.mitocode.repo;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.mitocode.model.Rol;

public interface IRolRepo extends IGenericRepo<Rol, Integer> {
	
	@Query(value="select r.id_rol,r.nombre,r.descripcion from rol r where r.id_rol =(select distinct(ur.id_rol) from menu_rol mr inner join usuario_rol ur on ur.id_rol = mr.id_rol inner join menu m on m.id_menu = mr.id_menu inner join usuario u on u.id_usuario = ur.id_usuario where u.nombre = :usuario)", nativeQuery = true)
	Rol rolPorUsuario(@Param("usuario")String usuario);

}
