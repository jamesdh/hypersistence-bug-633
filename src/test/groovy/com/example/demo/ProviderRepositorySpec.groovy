package com.example.demo

import com.querydsl.core.types.Predicate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.ContextConfiguration
import spock.lang.See
import spock.lang.Specification

@SpringBootTest
@ContextConfiguration
@Import(TestIntegrations)
class ProviderRepositorySpec extends Specification {

    @Autowired
    ProviderRepository providerRepository

    @See("https://github.com/vladmihalcea/hypersistence-utils/commit/5fe3d3b7102abd5e3b63694b45a6c16125cc269e")
    def "query failure caused by hypersistence-utils 3.4.4"() {
        given: "a mock provider"
        Provider user = providerRepository.save(new Provider(userType: 1, lastName: "user"))

        and: "a predicate to match the dummy provider"
        QProvider root = QProvider.provider
        Predicate predicate = root.userType.eq(1).and(root.lastName.containsIgnoreCase("user"))

        when: "querying for the predicate"
        List<Provider> providers = providerRepository.findAll(predicate)

        then: "should return our mock provider"
        providers?.size() == 1
        providers.first().id == user.id
    }
}
