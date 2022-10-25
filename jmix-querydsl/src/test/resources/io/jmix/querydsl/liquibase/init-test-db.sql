-- public.author_ definition

-- Drop table

-- DROP TABLE author_;

CREATE TABLE author_ (
	id int8 NOT NULL,
	"name" varchar(255) NULL,
	CONSTRAINT pk_author_ PRIMARY KEY (id)
);


-- public.bar_ definition

-- Drop table

-- DROP TABLE bar_;

CREATE TABLE bar_ (
	id int4 NOT NULL,
	"date" date NULL,
	CONSTRAINT pk_bar_ PRIMARY KEY (id)
);


-- public.calendar_ definition

-- Drop table

-- DROP TABLE calendar_;

CREATE TABLE calendar_ (
	id int4 NOT NULL,
	CONSTRAINT pk_calendar_ PRIMARY KEY (id)
);


-- public.catalog_ definition

-- Drop table

-- DROP TABLE catalog_;

CREATE TABLE catalog_ (
	id int4 NOT NULL,
	effective_date date NULL,
	CONSTRAINT pk_catalog_ PRIMARY KEY (id)
);


-- public.document_ definition

-- Drop table

-- DROP TABLE document_;

CREATE TABLE document_ (
	id int4 NOT NULL,
	"name" varchar(255) NULL,
	valid_to date NULL,
	CONSTRAINT pk_document_ PRIMARY KEY (id)
);


-- public.foo_ definition

-- Drop table

-- DROP TABLE foo_;

CREATE TABLE foo_ (
	id int4 NOT NULL,
	bar varchar(255) NULL,
	"date" date NULL,
	CONSTRAINT pk_foo_ PRIMARY KEY (id)
);


-- public.group_ definition

-- Drop table

-- DROP TABLE group_;

CREATE TABLE group_ (
	id int4 NOT NULL,
	"name" varchar(255) NULL,
	CONSTRAINT pk_group_ PRIMARY KEY (id)
);


-- public.inheritedproperties_ definition

-- Drop table

-- DROP TABLE inheritedproperties_;

CREATE TABLE inheritedproperties_ (
	id int8 NOT NULL,
	superclass_property varchar(255) NULL,
	string_as_simple varchar(255) NULL,
	class_property varchar(255) NULL,
	CONSTRAINT pk_inheritedproperties_ PRIMARY KEY (id)
);


-- public.location_ definition

-- Drop table

-- DROP TABLE location_;

CREATE TABLE location_ (
	id int8 NOT NULL,
	"name" varchar(255) NULL,
	CONSTRAINT pk_location_ PRIMARY KEY (id)
);


-- public.mammal definition

-- Drop table

-- DROP TABLE mammal;

CREATE TABLE mammal (
	id int8 NOT NULL,
	CONSTRAINT pk_mammal PRIMARY KEY (id)
);


-- public.name_ definition

-- Drop table

-- DROP TABLE name_;

CREATE TABLE name_ (
	id int8 NOT NULL,
	first_name varchar(255) NULL,
	last_name varchar(255) NULL,
	nick_name varchar(255) NULL,
	CONSTRAINT pk_name_ PRIMARY KEY (id)
);


-- public.named_ definition

-- Drop table

-- DROP TABLE named_;

CREATE TABLE named_ (
	id int8 NOT NULL,
	"name" varchar(255) NULL,
	CONSTRAINT pk_named_ PRIMARY KEY (id)
);


-- public.namelist_ definition

-- Drop table

-- DROP TABLE namelist_;

CREATE TABLE namelist_ (
	id int8 NOT NULL,
	CONSTRAINT pk_namelist_ PRIMARY KEY (id)
);


-- public.parameter_ definition

-- Drop table

-- DROP TABLE parameter_;

CREATE TABLE parameter_ (
	id int8 NOT NULL,
	CONSTRAINT pk_parameter_ PRIMARY KEY (id)
);


-- public.parent2 definition

-- Drop table

-- DROP TABLE parent2;

CREATE TABLE parent2 (
	id int4 NOT NULL,
	CONSTRAINT pk_parent2 PRIMARY KEY (id)
);


-- public.persistent_logins definition

-- Drop table

-- DROP TABLE persistent_logins;

CREATE TABLE persistent_logins (
	series varchar(64) NOT NULL,
	username varchar(64) NOT NULL,
	"token" varchar(64) NOT NULL,
	last_used timestamp NOT NULL,
	CONSTRAINT persistent_logins_pkey PRIMARY KEY (series)
);


-- public.personid_ definition

-- Drop table

-- DROP TABLE personid_;

CREATE TABLE personid_ (
	id int8 NOT NULL,
	country varchar(255) NULL,
	medicare_number int4 NULL,
	CONSTRAINT pk_personid_ PRIMARY KEY (id)
);


-- public.player_ definition

-- Drop table

-- DROP TABLE player_;

CREATE TABLE player_ (
	id int8 NOT NULL,
	CONSTRAINT pk_player_ PRIMARY KEY (id)
);


-- public.querydsl_cuba_entity1 definition

-- Drop table

-- DROP TABLE querydsl_cuba_entity1;

CREATE TABLE querydsl_cuba_entity1 (
	id uuid NOT NULL,
	int_id int4 NULL,
	property varchar(255) NULL,
	property2 varchar(255) NULL,
	dtype varchar(100) NULL,
	CONSTRAINT pk_querydsl_cuba_entity1 PRIMARY KEY (id)
);


-- public.querydsl_cuba_numeric definition

-- Drop table

-- DROP TABLE querydsl_cuba_numeric;

CREATE TABLE querydsl_cuba_numeric (
	id uuid NOT NULL,
	long_id int8 NULL,
	value numeric NULL,
	CONSTRAINT pk_querydsl_cuba_numeric PRIMARY KEY (id)
);


-- public.sec_resource_role definition

-- Drop table

-- DROP TABLE sec_resource_role;

CREATE TABLE sec_resource_role (
	id uuid NOT NULL,
	"version" int4 NOT NULL DEFAULT 1,
	create_ts timestamp NULL,
	created_by varchar(50) NULL,
	update_ts timestamp NULL,
	updated_by varchar(50) NULL,
	delete_ts timestamp NULL,
	deleted_by varchar(50) NULL,
	"name" varchar(255) NOT NULL,
	code varchar(255) NOT NULL,
	child_roles text NULL,
	sys_tenant_id varchar(255) NULL,
	description text NULL,
	scopes varchar(1000) NULL,
	CONSTRAINT "SEC_RESOURCE_ROLE_pkey" PRIMARY KEY (id)
);
CREATE INDEX idx_resource_role_un_c ON public.sec_resource_role USING btree (code) WHERE (delete_ts IS NULL);


-- public.sec_role_assignment definition

-- Drop table

-- DROP TABLE sec_role_assignment;

CREATE TABLE sec_role_assignment (
	id uuid NOT NULL,
	"version" int4 NOT NULL DEFAULT 1,
	create_ts timestamp NULL,
	created_by varchar(50) NULL,
	update_ts timestamp NULL,
	updated_by varchar(50) NULL,
	delete_ts timestamp NULL,
	deleted_by varchar(50) NULL,
	username varchar(255) NOT NULL,
	role_code varchar(255) NOT NULL,
	role_type varchar(255) NOT NULL,
	CONSTRAINT "SEC_ROLE_ASSIGNMENT_pkey" PRIMARY KEY (id)
);


-- public.sec_row_level_role definition

-- Drop table

-- DROP TABLE sec_row_level_role;

CREATE TABLE sec_row_level_role (
	id uuid NOT NULL,
	"version" int4 NOT NULL DEFAULT 1,
	create_ts timestamp NULL,
	created_by varchar(50) NULL,
	update_ts timestamp NULL,
	updated_by varchar(50) NULL,
	delete_ts timestamp NULL,
	deleted_by varchar(50) NULL,
	"name" varchar(255) NOT NULL,
	code varchar(255) NOT NULL,
	child_roles text NULL,
	sys_tenant_id varchar(255) NULL,
	description text NULL,
	CONSTRAINT "SEC_ROW_LEVEL_ROLE_pkey" PRIMARY KEY (id)
);
CREATE INDEX idx_row_level_role_un_c ON public.sec_row_level_role USING btree (code) WHERE (delete_ts IS NULL);


-- public.sec_user_substitution definition

-- Drop table

-- DROP TABLE sec_user_substitution;

CREATE TABLE sec_user_substitution (
	id uuid NOT NULL,
	"version" int4 NOT NULL DEFAULT 1,
	create_ts timestamp NULL,
	created_by varchar(50) NULL,
	update_ts timestamp NULL,
	updated_by varchar(50) NULL,
	delete_ts timestamp NULL,
	deleted_by varchar(50) NULL,
	username varchar(255) NOT NULL,
	substituted_username varchar(255) NOT NULL,
	start_date timestamp NULL,
	end_date timestamp NULL,
	CONSTRAINT "SEC_USER_SUBSTITUTION_pkey" PRIMARY KEY (id)
);


-- public.status_ definition

-- Drop table

-- DROP TABLE status_;

CREATE TABLE status_ (
	id int8 NOT NULL,
	"name" varchar(255) NULL,
	CONSTRAINT pk_status_ PRIMARY KEY (id)
);


-- public.statuschange_ definition

-- Drop table

-- DROP TABLE statuschange_;

CREATE TABLE statuschange_ (
	id int8 NOT NULL,
	"time_stamp" timestamp NULL,
	CONSTRAINT pk_statuschange_ PRIMARY KEY (id)
);


-- public.ui_filter_configuration definition

-- Drop table

-- DROP TABLE ui_filter_configuration;

CREATE TABLE ui_filter_configuration (
	id uuid NOT NULL,
	component_id varchar(255) NOT NULL,
	configuration_id varchar(255) NOT NULL,
	username varchar(255) NULL,
	root_condition text NULL,
	sys_tenant_id varchar(255) NULL,
	"name" varchar(255) NULL,
	default_for_all bool NULL,
	CONSTRAINT "UI_FILTER_CONFIGURATION_pkey" PRIMARY KEY (id)
);


-- public.ui_setting definition

-- Drop table

-- DROP TABLE ui_setting;

CREATE TABLE ui_setting (
	id uuid NOT NULL,
	create_ts timestamp NULL,
	created_by varchar(50) NULL,
	username varchar(255) NULL,
	"name" varchar(255) NULL,
	value_ text NULL,
	CONSTRAINT "UI_SETTING_pkey" PRIMARY KEY (id)
);


-- public.ui_table_presentation definition

-- Drop table

-- DROP TABLE ui_table_presentation;

CREATE TABLE ui_table_presentation (
	id uuid NOT NULL,
	create_ts timestamp NULL,
	created_by varchar(50) NULL,
	component varchar(255) NULL,
	"name" varchar(255) NULL,
	settings varchar(4000) NULL,
	username varchar(255) NULL,
	is_auto_save bool NULL,
	update_ts timestamp NULL,
	updated_by varchar(50) NULL,
	sys_tenant_id varchar(255) NULL,
	is_default bool NULL,
	CONSTRAINT "UI_TABLE_PRESENTATION_pkey" PRIMARY KEY (id)
);


-- public.usr_user definition

-- Drop table

-- DROP TABLE usr_user;

CREATE TABLE usr_user (
	id uuid NOT NULL,
	"version" int4 NOT NULL,
	username varchar(255) NOT NULL,
	first_name varchar(255) NULL,
	last_name varchar(255) NULL,
	"password" varchar(255) NULL,
	email varchar(255) NULL,
	active bool NULL,
	time_zone_id varchar(255) NULL,
	CONSTRAINT "USR_USER_pkey" PRIMARY KEY (id)
);
CREATE UNIQUE INDEX idx_user__on_username ON public.usr_user USING btree (username);


-- public.world definition

-- Drop table

-- DROP TABLE world;

CREATE TABLE world (
	id int8 NOT NULL,
	CONSTRAINT pk_world PRIMARY KEY (id)
);


-- public.book_ definition

-- Drop table

-- DROP TABLE book_;

CREATE TABLE book_ (
	id int8 NOT NULL,
	title varchar(255) NULL,
	author_id int8 NULL,
	CONSTRAINT pk_book_ PRIMARY KEY (id),
	CONSTRAINT fk_book__on_author FOREIGN KEY (author_id) REFERENCES author_(id)
);


-- public.child2 definition

-- Drop table

-- DROP TABLE child2;

CREATE TABLE child2 (
	id int4 NOT NULL,
	parent_id int4 NULL,
	CONSTRAINT pk_child2 PRIMARY KEY (id),
	CONSTRAINT fk_child2_on_parent FOREIGN KEY (parent_id) REFERENCES parent2(id)
);


-- public.eviltype_ definition

-- Drop table

-- DROP TABLE eviltype_;

CREATE TABLE eviltype_ (
	id int4 NOT NULL,
	"_asc" int4 NULL,
	"_desc" int4 NULL,
	isnull_id int4 NULL,
	isnotnull_id int4 NULL,
	get_id int4 NULL,
	get_type_id int4 NULL,
	get_metadata_id int4 NULL,
	to_string_id int4 NULL,
	hash_code_id int4 NULL,
	get_class_id int4 NULL,
	notify_id int4 NULL,
	notify_all_id int4 NULL,
	wait_id int4 NULL,
	CONSTRAINT pk_eviltype_ PRIMARY KEY (id),
	CONSTRAINT fk_eviltype__on__asc FOREIGN KEY ("_asc") REFERENCES eviltype_(id),
	CONSTRAINT fk_eviltype__on__desc FOREIGN KEY ("_desc") REFERENCES eviltype_(id),
	CONSTRAINT fk_eviltype__on_get FOREIGN KEY (get_id) REFERENCES eviltype_(id),
	CONSTRAINT fk_eviltype__on_getclass FOREIGN KEY (get_class_id) REFERENCES eviltype_(id),
	CONSTRAINT fk_eviltype__on_getmetadata FOREIGN KEY (get_metadata_id) REFERENCES eviltype_(id),
	CONSTRAINT fk_eviltype__on_gettype FOREIGN KEY (get_type_id) REFERENCES eviltype_(id),
	CONSTRAINT fk_eviltype__on_hashcode FOREIGN KEY (hash_code_id) REFERENCES eviltype_(id),
	CONSTRAINT fk_eviltype__on_isnotnull FOREIGN KEY (isnotnull_id) REFERENCES eviltype_(id),
	CONSTRAINT fk_eviltype__on_isnull FOREIGN KEY (isnull_id) REFERENCES eviltype_(id),
	CONSTRAINT fk_eviltype__on_notify FOREIGN KEY (notify_id) REFERENCES eviltype_(id),
	CONSTRAINT fk_eviltype__on_notifyall FOREIGN KEY (notify_all_id) REFERENCES eviltype_(id),
	CONSTRAINT fk_eviltype__on_tostring FOREIGN KEY (to_string_id) REFERENCES eviltype_(id),
	CONSTRAINT fk_eviltype__on_wait FOREIGN KEY (wait_id) REFERENCES eviltype_(id)
);


-- public.formula_ definition

-- Drop table

-- DROP TABLE formula_;

CREATE TABLE formula_ (
	id int4 NOT NULL,
	parameter_id int8 NULL,
	CONSTRAINT pk_formula_ PRIMARY KEY (id),
	CONSTRAINT fk_formula__on_parameter FOREIGN KEY (parameter_id) REFERENCES parameter_(id)
);


-- public.item_ definition

-- Drop table

-- DROP TABLE item_;

CREATE TABLE item_ (
	id int8 NOT NULL,
	product_id int8 NULL,
	"name" varchar(255) NULL,
	current_status_id int8 NULL,
	status_id int8 NULL,
	payment_status int4 NULL,
	CONSTRAINT pk_item_ PRIMARY KEY (id),
	CONSTRAINT fk_item__on_currentstatus FOREIGN KEY (current_status_id) REFERENCES status_(id),
	CONSTRAINT fk_item__on_product FOREIGN KEY (product_id) REFERENCES item_(id),
	CONSTRAINT fk_item__on_status FOREIGN KEY (status_id) REFERENCES status_(id)
);


-- public.nationality_ definition

-- Drop table

-- DROP TABLE nationality_;

CREATE TABLE nationality_ (
	id int8 NOT NULL,
	calendar_id int4 NULL,
	CONSTRAINT pk_nationality_ PRIMARY KEY (id),
	CONSTRAINT fk_nationality__on_calendar FOREIGN KEY (calendar_id) REFERENCES calendar_(id)
);


-- public.null_status_changes definition

-- Drop table

-- DROP TABLE null_status_changes;

CREATE TABLE null_status_changes (
	payment_id int8 NOT NULL,
	status_changes_id int8 NOT NULL,
	CONSTRAINT uc_null_status_changes_statuschanges UNIQUE (status_changes_id),
	CONSTRAINT fk_nulstacha_on_payment FOREIGN KEY (payment_id) REFERENCES item_(id),
	CONSTRAINT fk_nulstacha_on_status_change FOREIGN KEY (status_changes_id) REFERENCES statuschange_(id)
);


-- public.person_ definition

-- Drop table

-- DROP TABLE person_;

CREATE TABLE person_ (
	i int8 NOT NULL,
	birth_day date NULL,
	pid_id int8 NULL,
	"name" varchar(255) NULL,
	nationality_id int8 NULL,
	CONSTRAINT pk_person_ PRIMARY KEY (i),
	CONSTRAINT fk_person__on_nationality FOREIGN KEY (nationality_id) REFERENCES nationality_(id),
	CONSTRAINT fk_person__on_pid FOREIGN KEY (pid_id) REFERENCES personid_(id)
);


-- public.price_ definition

-- Drop table

-- DROP TABLE price_;

CREATE TABLE price_ (
	id int8 NOT NULL,
	amount int8 NULL,
	product_id int8 NULL,
	CONSTRAINT pk_price_ PRIMARY KEY (id),
	CONSTRAINT fk_price__on_product FOREIGN KEY (product_id) REFERENCES item_(id)
);


-- public.querydsl_cuba_animal definition

-- Drop table

-- DROP TABLE querydsl_cuba_animal;

CREATE TABLE querydsl_cuba_animal (
	id uuid NOT NULL,
	int_id int4 NULL,
	alive bool NULL,
	birthdate timestamp NULL,
	weight int4 NULL,
	toes int4 NULL,
	body_weight float8 NULL,
	double_property float8 NULL,
	color varchar(255) NULL,
	date_field date NULL,
	"name" varchar(255) NULL,
	breed int4 NULL,
	eyecolor varchar(255) NULL,
	cat_id uuid NULL,
	dtype varchar(100) NULL,
	time_field time NULL,
	CONSTRAINT pk_querydsl_cuba_animal PRIMARY KEY (id),
	CONSTRAINT uc_querydsl_cuba_animal_int UNIQUE (int_id),
	CONSTRAINT fk_querydsl_cuba_animal_on_cat FOREIGN KEY (cat_id) REFERENCES querydsl_cuba_animal(id)
);


-- public.querydsl_cuba_kittens_set definition

-- Drop table

-- DROP TABLE querydsl_cuba_kittens_set;

CREATE TABLE querydsl_cuba_kittens_set (
	cat_id uuid NOT NULL,
	kitten_id uuid NOT NULL,
	CONSTRAINT pk_querydsl_cuba_kittens_set PRIMARY KEY (cat_id, kitten_id),
	CONSTRAINT uc_querydsl_cuba_kittens_set_kitten UNIQUE (kitten_id),
	CONSTRAINT fk_quecubkitset_on_cat FOREIGN KEY (kitten_id) REFERENCES querydsl_cuba_animal(id),
	CONSTRAINT fk_quecubkitset_on_domestic_cat FOREIGN KEY (cat_id) REFERENCES querydsl_cuba_animal(id)
);


-- public.querydsl_cuba_show definition

-- Drop table

-- DROP TABLE querydsl_cuba_show;

CREATE TABLE querydsl_cuba_show (
	id uuid NOT NULL,
	int_id int8 NULL,
	parent_id uuid NULL,
	CONSTRAINT pk_querydsl_cuba_show PRIMARY KEY (id),
	CONSTRAINT uc_querydsl_cuba_show_int UNIQUE (int_id),
	CONSTRAINT fk_querydsl_cuba_show_on_parent FOREIGN KEY (parent_id) REFERENCES querydsl_cuba_show(id)
);


-- public.sec_resource_policy definition

-- Drop table

-- DROP TABLE sec_resource_policy;

CREATE TABLE sec_resource_policy (
	id uuid NOT NULL,
	"version" int4 NOT NULL DEFAULT 1,
	create_ts timestamp NULL,
	created_by varchar(50) NULL,
	update_ts timestamp NULL,
	updated_by varchar(50) NULL,
	delete_ts timestamp NULL,
	deleted_by varchar(50) NULL,
	type_ varchar(255) NOT NULL,
	policy_group varchar(255) NULL,
	resource_ varchar(1000) NOT NULL,
	action_ varchar(255) NOT NULL,
	effect varchar(255) NOT NULL,
	role_id uuid NOT NULL,
	CONSTRAINT "SEC_RESOURCE_POLICY_pkey" PRIMARY KEY (id),
	CONSTRAINT fk_res_policy_role FOREIGN KEY (role_id) REFERENCES sec_resource_role(id)
);


-- public.sec_row_level_policy definition

-- Drop table

-- DROP TABLE sec_row_level_policy;

CREATE TABLE sec_row_level_policy (
	id uuid NOT NULL,
	"version" int4 NOT NULL DEFAULT 1,
	create_ts timestamp NULL,
	created_by varchar(50) NULL,
	update_ts timestamp NULL,
	updated_by varchar(50) NULL,
	delete_ts timestamp NULL,
	deleted_by varchar(50) NULL,
	type_ varchar(255) NOT NULL,
	action_ varchar(255) NOT NULL,
	entity_name varchar(255) NOT NULL,
	where_clause text NULL,
	join_clause text NULL,
	script_ text NULL,
	role_id uuid NOT NULL,
	CONSTRAINT "SEC_ROW_LEVEL_POLICY_pkey" PRIMARY KEY (id),
	CONSTRAINT fk_row_level_policy_role FOREIGN KEY (role_id) REFERENCES sec_row_level_role(id)
);


-- public.store_ definition

-- Drop table

-- DROP TABLE store_;

CREATE TABLE store_ (
	id int8 NOT NULL,
	location_id int8 NULL,
	CONSTRAINT pk_store_ PRIMARY KEY (id),
	CONSTRAINT fk_store__on_location FOREIGN KEY (location_id) REFERENCES location_(id)
);


-- public.world_mammals definition

-- Drop table

-- DROP TABLE world_mammals;

CREATE TABLE world_mammals (
	world_id int8 NOT NULL,
	mammals_id int8 NOT NULL,
	CONSTRAINT pk_world_mammals PRIMARY KEY (world_id, mammals_id),
	CONSTRAINT uc_world_mammals_mammals UNIQUE (mammals_id),
	CONSTRAINT fk_wormam_on_mammal FOREIGN KEY (mammals_id) REFERENCES mammal(id),
	CONSTRAINT fk_wormam_on_world FOREIGN KEY (world_id) REFERENCES world(id)
);


-- public.account_ definition

-- Drop table

-- DROP TABLE account_;

CREATE TABLE account_ (
	id int8 NOT NULL,
	owner_i int8 NULL,
	some_data varchar(255) NULL,
	CONSTRAINT pk_account_ PRIMARY KEY (id),
	CONSTRAINT fk_account__on_owner_i FOREIGN KEY (owner_i) REFERENCES person_(i)
);


-- public.auditlog_ definition

-- Drop table

-- DROP TABLE auditlog_;

CREATE TABLE auditlog_ (
	id int4 NOT NULL,
	item_id int8 NULL,
	CONSTRAINT pk_auditlog_ PRIMARY KEY (id),
	CONSTRAINT fk_auditlog__on_item FOREIGN KEY (item_id) REFERENCES item_(id)
);


-- public.catalog__prices definition

-- Drop table

-- DROP TABLE catalog__prices;

CREATE TABLE catalog__prices (
	catalog_id int4 NOT NULL,
	prices_id int8 NOT NULL,
	CONSTRAINT pk_catalog__prices PRIMARY KEY (catalog_id, prices_id),
	CONSTRAINT uc_catalog__prices_prices UNIQUE (prices_id),
	CONSTRAINT fk_catpri_on_catalog FOREIGN KEY (catalog_id) REFERENCES catalog_(id),
	CONSTRAINT fk_catpri_on_price FOREIGN KEY (prices_id) REFERENCES price_(id)
);


-- public.customer_ definition

-- Drop table

-- DROP TABLE customer_;

CREATE TABLE customer_ (
	id int4 NOT NULL,
	current_order_id int8 NULL,
	name_id int8 NULL,
	CONSTRAINT pk_customer_ PRIMARY KEY (id)
);


-- public.line_items definition

-- Drop table

-- DROP TABLE line_items;

CREATE TABLE line_items (
	order_id int8 NOT NULL,
	"_index" int4 NOT NULL,
	line_items_id int8 NOT NULL,
	CONSTRAINT pk_lineitems PRIMARY KEY (order_id, _index),
	CONSTRAINT uc_line_items_lineitems UNIQUE (line_items_id)
);


-- public.line_items2 definition

-- Drop table

-- DROP TABLE line_items2;

CREATE TABLE line_items2 (
	order_id int8 NOT NULL,
	line_items_map_id int8 NOT NULL,
	CONSTRAINT pk_lineitems2 PRIMARY KEY (order_id, line_items_map_id),
	CONSTRAINT uc_line_items2_lineitemsmap UNIQUE (line_items_map_id)
);


-- public.order_ definition

-- Drop table

-- DROP TABLE order_;

CREATE TABLE order_ (
	id int8 NOT NULL,
	customer_id int4 NULL,
	"null" bool NULL,
	CONSTRAINT pk_order_ PRIMARY KEY (id)
);


-- public.order__items definition

-- Drop table

-- DROP TABLE order__items;

CREATE TABLE order__items (
	order_id int8 NOT NULL,
	"_index" int4 NOT NULL,
	items_id int8 NOT NULL,
	CONSTRAINT pk_order__items PRIMARY KEY (order_id, _index),
	CONSTRAINT uc_order__items_items UNIQUE (items_id)
);


-- public.querydsl_cuba_company definition

-- Drop table

-- DROP TABLE querydsl_cuba_company;

CREATE TABLE querydsl_cuba_company (
	id uuid NOT NULL,
	int_id int4 NULL,
	"name" varchar(255) NULL,
	official_name varchar(255) NULL,
	rating_ordinal varchar(255) NULL,
	rating_string varchar(255) NULL,
	employee_id uuid NULL,
	CONSTRAINT pk_querydsl_cuba_company PRIMARY KEY (id),
	CONSTRAINT uc_querydsl_cuba_company_int UNIQUE (int_id)
);


-- public.querydsl_cuba_department definition

-- Drop table

-- DROP TABLE querydsl_cuba_department;

CREATE TABLE querydsl_cuba_department (
	id uuid NOT NULL,
	"name" varchar(255) NULL,
	company_id uuid NULL,
	CONSTRAINT pk_querydsl_cuba_department PRIMARY KEY (id)
);


-- public.querydsl_cuba_department_employees definition

-- Drop table

-- DROP TABLE querydsl_cuba_department_employees;

CREATE TABLE querydsl_cuba_department_employees (
	employees_id uuid NOT NULL,
	querydslcuba_department_id uuid NOT NULL,
	CONSTRAINT uc_querydsl_cuba_department_employees_employees UNIQUE (employees_id)
);


-- public.querydsl_cuba_employee definition

-- Drop table

-- DROP TABLE querydsl_cuba_employee;

CREATE TABLE querydsl_cuba_employee (
	id uuid NOT NULL,
	int_id int4 NULL,
	first_name varchar(255) NULL,
	last_name varchar(255) NULL,
	company_id uuid NULL,
	user_id uuid NULL,
	CONSTRAINT pk_querydsl_cuba_employee PRIMARY KEY (id)
);


-- public.querydsl_cuba_user definition

-- Drop table

-- DROP TABLE querydsl_cuba_user;

CREATE TABLE querydsl_cuba_user (
	id uuid NOT NULL,
	user_name varchar(255) NULL,
	first_name varchar(255) NULL,
	last_name varchar(255) NULL,
	company_id uuid NULL,
	CONSTRAINT pk_querydsl_cuba_user PRIMARY KEY (id)
);


-- public.store__customers definition

-- Drop table

-- DROP TABLE store__customers;

CREATE TABLE store__customers (
	store_id int8 NOT NULL,
	customers_id int4 NOT NULL,
	CONSTRAINT uc_store__customers_customers UNIQUE (customers_id)
);


-- public.customer_ foreign keys

ALTER TABLE public.customer_ ADD CONSTRAINT fk_customer__on_currentorder FOREIGN KEY (current_order_id) REFERENCES order_(id);
ALTER TABLE public.customer_ ADD CONSTRAINT fk_customer__on_name FOREIGN KEY (name_id) REFERENCES name_(id);


-- public.line_items foreign keys

ALTER TABLE public.line_items ADD CONSTRAINT fk_linite_on_itembibauj FOREIGN KEY (line_items_id) REFERENCES item_(id);
ALTER TABLE public.line_items ADD CONSTRAINT fk_linite_on_order3op0vw FOREIGN KEY (order_id) REFERENCES order_(id);


-- public.line_items2 foreign keys

ALTER TABLE public.line_items2 ADD CONSTRAINT fk_linite_on_item FOREIGN KEY (line_items_map_id) REFERENCES item_(id);
ALTER TABLE public.line_items2 ADD CONSTRAINT fk_linite_on_order FOREIGN KEY (order_id) REFERENCES order_(id);


-- public.order_ foreign keys

ALTER TABLE public.order_ ADD CONSTRAINT fk_order__on_customer FOREIGN KEY (customer_id) REFERENCES customer_(id);


-- public.order__items foreign keys

ALTER TABLE public.order__items ADD CONSTRAINT fk_ordite_on_item FOREIGN KEY (items_id) REFERENCES item_(id);
ALTER TABLE public.order__items ADD CONSTRAINT fk_ordite_on_order FOREIGN KEY (order_id) REFERENCES order_(id);


-- public.querydsl_cuba_company foreign keys

ALTER TABLE public.querydsl_cuba_company ADD CONSTRAINT fk_querydsl_cuba_company_on_employee FOREIGN KEY (employee_id) REFERENCES querydsl_cuba_employee(id);


-- public.querydsl_cuba_department foreign keys

ALTER TABLE public.querydsl_cuba_department ADD CONSTRAINT fk_querydsl_cuba_department_on_company FOREIGN KEY (company_id) REFERENCES querydsl_cuba_company(id);


-- public.querydsl_cuba_department_employees foreign keys

ALTER TABLE public.querydsl_cuba_department_employees ADD CONSTRAINT fk_quecubdepemp_on_department FOREIGN KEY (querydslcuba_department_id) REFERENCES querydsl_cuba_department(id);
ALTER TABLE public.querydsl_cuba_department_employees ADD CONSTRAINT fk_quecubdepemp_on_employee FOREIGN KEY (employees_id) REFERENCES querydsl_cuba_employee(id);


-- public.querydsl_cuba_employee foreign keys

ALTER TABLE public.querydsl_cuba_employee ADD CONSTRAINT fk_querydsl_cuba_employee_on_company FOREIGN KEY (company_id) REFERENCES querydsl_cuba_company(id);
ALTER TABLE public.querydsl_cuba_employee ADD CONSTRAINT fk_querydsl_cuba_employee_on_user FOREIGN KEY (user_id) REFERENCES querydsl_cuba_user(id);


-- public.querydsl_cuba_user foreign keys

ALTER TABLE public.querydsl_cuba_user ADD CONSTRAINT fk_querydsl_cuba_user_on_company FOREIGN KEY (company_id) REFERENCES querydsl_cuba_company(id);


-- public.store__customers foreign keys

ALTER TABLE public.store__customers ADD CONSTRAINT fk_stocus_on_customer FOREIGN KEY (customers_id) REFERENCES customer_(id);
ALTER TABLE public.store__customers ADD CONSTRAINT fk_stocus_on_store FOREIGN KEY (store_id) REFERENCES store_(id);