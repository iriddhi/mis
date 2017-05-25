package com.realworld.shared.models

/**
  *.
  */
/////////////////////////////////////////////////////
// Users
/////////////////////////////////////////////////////
case class User(id: User.Id, roleId: Role.Id, created: User.Created)

object User {
  def create(id: java.util.UUID, roleId: java.util.UUID, created: java.time.LocalDateTime): User = {
    User(Id(id), Role.Id(roleId), Created(created))
  }

  case class Id(value: java.util.UUID) extends AnyVal

  case class Created(value: java.time.LocalDateTime) extends AnyVal

}
