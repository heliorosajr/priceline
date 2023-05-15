USE priceline;

-- Role
CREATE TABLE IF NOT EXISTS role (
    id BIGINT NOT NULL AUTO_INCREMENT,
    uid VARCHAR(40) NOT NULL,
    name VARCHAR(150) NOT NULL,
    default_role BOOLEAN NOT NULL,
    CONSTRAINT PK_role_id PRIMARY KEY (id),
    CONSTRAINT UK_role_uid UNIQUE (uid),
    CONSTRAINT UK_role_name UNIQUE (name)
);

-- Membership
CREATE TABLE IF NOT EXISTS membership (
    id BIGINT NOT NULL AUTO_INCREMENT,
    uid VARCHAR(40) NOT NULL,
    user_id VARCHAR(40) NOT NULL,
    team_id VARCHAR(40) NOT NULL,
    role_id BIGINT NOT NULL,
    CONSTRAINT PK_membership_id PRIMARY KEY (id),
    CONSTRAINT FK_membership_role FOREIGN KEY (role_id) REFERENCES role(id),
    CONSTRAINT UK_membership_userTeamRole UNIQUE (user_id, team_id, role_id)
);