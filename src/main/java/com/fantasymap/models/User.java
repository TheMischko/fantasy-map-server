package com.fantasymap.models;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.IdName;
import org.javalite.activejdbc.annotations.Table;

@Table("public.user")
@IdName("user_id")
public class User extends Model {
}
