package shop.nongdam.nongdambackend.global.config;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.test.context.ActiveProfiles;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@DisplayName("MySQL 로컬 연동 테스트")
@ActiveProfiles("test")
public class DatabaseConnectionTest {

    @Autowired
    private DataSource dataSource;

    @DisplayName("MySQL 연결 성공 및 주요 테이블 존재 확인")
    @Test
    void givenMySQLLocal_whenConnection_thenTablesExist() throws SQLException {
        // given
        Connection conn = DataSourceUtils.getConnection(dataSource);

        // when
        ResultSet resultSet = conn.getMetaData()
                .getTables(null, null, null, new String[]{"TABLE"});
        System.out.println("resultSet = " + resultSet);

        boolean hasFarmTable = false;
        boolean hasFarmBadge = false;
        boolean hasFarmBadgeAssignment = false;
        boolean hasFarmBadgeAssignmentId = false;

        boolean hasIngredient = false;
        boolean hasIngredientCategory = false;
        boolean hasIngredientImage = false;
        boolean hasIngredientUglyReason = false;

        boolean hasMember = false;
        boolean hasRole = false;
        boolean hasSocialType = false;

        boolean hasRegion = false;

        boolean hasRestaurant = false;

        while (resultSet.next()) {
            String tableName = resultSet.getString("TABLE_NAME").toLowerCase();
            if (tableName.equals("farm")) hasFarmTable = true;
            if (tableName.equals("farm_badge")) hasFarmBadge = true;
            if (tableName.equals("farm_badge_assignment")) hasFarmBadgeAssignment = true;
            if (tableName.equals("farm_badge_assignment_id")) hasFarmBadgeAssignmentId = true;
            if (tableName.equals("ingredient")) hasIngredient = true;
            if (tableName.equals("ingredient_category")) hasIngredientCategory = true;
            if (tableName.equals("ingredient_image")) hasIngredientImage = true;
            if (tableName.equals("ingredient_ugly_reason")) hasIngredientUglyReason = true;
            if (tableName.equals("member")) hasMember = true;
            if (tableName.equals("role")) hasRole = true;
            if (tableName.equals("social_type")) hasSocialType = true;
            if (tableName.equals("region")) hasRegion = true;
            if (tableName.equals("restaurant")) hasRestaurant = true;
        }

        resultSet.close();
        conn.close();

        // then
        assertThat(hasFarmTable).as("farm 테이블 존재 여부").isTrue();
        assertThat(hasFarmBadge).as("farm_badge 테이블 존재 여부").isTrue();
        assertThat(hasFarmBadgeAssignment).as("farm_badge_assignment 테이블 존재 여부").isTrue();
        assertThat(hasFarmBadgeAssignmentId).as("farm_badge_assignment_id 테이블 존재 여부").isTrue();
        assertThat(hasIngredient).as("ingredient 테이블 존재 여부").isTrue();
        assertThat(hasIngredientCategory).as("ingredient_category 테이블 존재 여부").isTrue();
        assertThat(hasIngredientImage).as("ingredient_image 테이블 존재 여부").isTrue();
        assertThat(hasIngredientUglyReason).as("ingredient_ugly_reason 테이블 존재 여부").isTrue();
        assertThat(hasMember).as("member 테이블 존재 여부").isTrue();
        assertThat(hasRole).as("role 테이블 존재 여부").isTrue();
        assertThat(hasSocialType).as("social_type 테이블 존재 여부").isTrue();
        assertThat(hasRegion).as("region 테이블 존재 여부").isTrue();
        assertThat(hasRestaurant).as("restaurant 테이블 존재 여부").isTrue();
    }
}
