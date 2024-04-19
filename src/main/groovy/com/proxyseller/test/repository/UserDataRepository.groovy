package com.proxyseller.test.repository

import com.proxyseller.test.model.UserData
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserDataRepository extends JpaRepository<UserData, Long> {

}
