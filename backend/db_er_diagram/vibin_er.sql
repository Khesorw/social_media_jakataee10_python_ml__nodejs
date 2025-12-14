create table if not exists user_info(

	id bigint generated always as identity primary key,
	user_name varchar(255) not null,
	user_pass varchar(255) not null,
	email varchar(255)

);


create table if not exists post(
id bigint generated always as identity primary key,
	
	user_id bigint not null,
	content_post text null,
	post_type varchar(20) not null,
	created_at timestamptz default NOW(),
	updated_at timestamptz, 
	constraint check_media_type check (post_type in ('text', 'image', 'video')),
	constraint fk_user_id foreign key (user_id) references user_info(id)
);


create table if not exists post_media(
id bigint generated always as identity primary key,
	post_id bigint not null,
	media_url text not null,
	media_type text not null,
	width int, 
	height int,
	duration int
	

);

create table if not exists comment_replies(
id bigint generated always as identity primary key,
	post_id bigint not null,
	content_text text not null,
	parent_comment_id bigint null,
	constraint fk_post_id foreign key (post_id) references post(id) on delete cascade,
	constraint fk_parent_id foreign key (parent_comment_id) references comment_replies(id) on delete cascade
);


create table if not exists conversation(
id bigint generated always as identity primary key,
	created_at timestamptz default NOW()

);

create table if not exists participant_conversation(
 conversation_id bigint not null,
 user_id bigint not null,
	constraint fk_conv foreign key (conversation_id) references conversation(id),
	constraint fk_user foreign key (user_id) references user_info(id),
	 primary key (conversation_id, user_id)

);

create table if not exists messages(
id bigint generated always as identity,
	message_txt text not null,
	created_at timestamptz default NOW(),
	conversation_id bigint not null,
	sender_id bigint not null,
	constraint fk_conv_id foreign key (conversation_id) references conversation(id),
	constraint fk_sender foreign key (sender_id) references user_info(id)
	
);




