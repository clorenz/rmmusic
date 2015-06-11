--
-- PostgreSQL database dump
--

SET statement_timeout = 0;
SET lock_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;

--
-- Name: plpgsql; Type: EXTENSION; Schema: -; Owner: 
--

CREATE EXTENSION IF NOT EXISTS plpgsql WITH SCHEMA pg_catalog;


--
-- Name: EXTENSION plpgsql; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION plpgsql IS 'PL/pgSQL procedural language';


SET search_path = public, pg_catalog;

SET default_tablespace = '';

SET default_with_oids = true;

--
-- Name: artist; Type: TABLE; Schema: public; Owner: rmmusic; Tablespace: 
--

CREATE TABLE artist (
    id integer NOT NULL,
    name text NOT NULL,
    print text NOT NULL,
    birthday date,
    country character(3),
    location text,
    url text,
    remarks text,
    "timestamp" timestamp without time zone NOT NULL
);


ALTER TABLE public.artist OWNER TO rmmusic;

--
-- Name: artist_id_seq; Type: SEQUENCE; Schema: public; Owner: rmmusic
--

CREATE SEQUENCE artist_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.artist_id_seq OWNER TO rmmusic;

--
-- Name: artist_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: rmmusic
--

ALTER SEQUENCE artist_id_seq OWNED BY artist.id;


--
-- Name: db_info; Type: TABLE; Schema: public; Owner: rmmusic; Tablespace: 
--

CREATE TABLE db_info (
    version character(10) NOT NULL
);


ALTER TABLE public.db_info OWNER TO rmmusic;

--
-- Name: medium; Type: TABLE; Schema: public; Owner: rmmusic; Tablespace: 
--

CREATE TABLE medium (
    id integer NOT NULL,
    type smallint NOT NULL,
    code character(8) NOT NULL,
    artist_id integer,
    title text,
    label text,
    ordercode text,
    p_year integer,
    size smallint,
    manufacturer text,
    system text,
    rec_begin_date date,
    rec_begin_b date,
    rec_end_date date,
    burning_date date,
    discid bigint,
    track_offsets character varying(255),
    category character varying(16),
    id3_genre character varying(16),
    digital character(3),
    audio boolean,
    rewritable boolean,
    magic text,
    files_type text,
    buy_date date,
    buy_price double precision,
    remarks text,
    "timestamp" timestamp without time zone NOT NULL
);


ALTER TABLE public.medium OWNER TO rmmusic;

--
-- Name: label; Type: VIEW; Schema: public; Owner: rmmusic
--

CREATE VIEW label AS
 SELECT DISTINCT medium.label
   FROM medium;


ALTER TABLE public.label OWNER TO rmmusic;

--
-- Name: medium_id_seq; Type: SEQUENCE; Schema: public; Owner: rmmusic
--

CREATE SEQUENCE medium_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.medium_id_seq OWNER TO rmmusic;

--
-- Name: medium_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: rmmusic
--

ALTER SEQUENCE medium_id_seq OWNED BY medium.id;


--
-- Name: recording; Type: TABLE; Schema: public; Owner: rmmusic; Tablespace: 
--

CREATE TABLE recording (
    id integer NOT NULL,
    song_id integer NOT NULL,
    medium_id integer NOT NULL,
    side character(1),
    track integer,
    counter text,
    "time" smallint,
    recording_year smallint,
    longplay character(2),
    quality integer,
    remarks text,
    special smallint,
    digital character(3),
    "timestamp" timestamp without time zone NOT NULL
);


ALTER TABLE public.recording OWNER TO rmmusic;

--
-- Name: recording_id_seq; Type: SEQUENCE; Schema: public; Owner: rmmusic
--

CREATE SEQUENCE recording_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.recording_id_seq OWNER TO rmmusic;

--
-- Name: recording_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: rmmusic
--

ALTER SEQUENCE recording_id_seq OWNED BY recording.id;


--
-- Name: song; Type: TABLE; Schema: public; Owner: rmmusic; Tablespace: 
--

CREATE TABLE song (
    id integer NOT NULL,
    artist_id integer NOT NULL,
    title text NOT NULL,
    release character(3),
    year smallint,
    authors text,
    dance character(2),
    id3genre character varying(24),
    remarks text,
    "timestamp" timestamp without time zone NOT NULL
);


ALTER TABLE public.song OWNER TO rmmusic;

--
-- Name: song_id_seq; Type: SEQUENCE; Schema: public; Owner: rmmusic
--

CREATE SEQUENCE song_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.song_id_seq OWNER TO rmmusic;

--
-- Name: song_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: rmmusic
--

ALTER SEQUENCE song_id_seq OWNED BY song.id;


--
-- Name: tag_medium; Type: TABLE; Schema: public; Owner: rmmusic; Tablespace: 
--

CREATE TABLE tag_medium (
    username character varying(64),
    mediumid integer,
    action character varying(64)
);


ALTER TABLE public.tag_medium OWNER TO rmmusic;

--
-- Name: id; Type: DEFAULT; Schema: public; Owner: rmmusic
--

ALTER TABLE ONLY artist ALTER COLUMN id SET DEFAULT nextval('artist_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: rmmusic
--

ALTER TABLE ONLY medium ALTER COLUMN id SET DEFAULT nextval('medium_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: rmmusic
--

ALTER TABLE ONLY recording ALTER COLUMN id SET DEFAULT nextval('recording_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: rmmusic
--

ALTER TABLE ONLY song ALTER COLUMN id SET DEFAULT nextval('song_id_seq'::regclass);


--
-- Name: artist_pkey; Type: CONSTRAINT; Schema: public; Owner: rmmusic; Tablespace: 
--

ALTER TABLE ONLY artist
    ADD CONSTRAINT artist_pkey PRIMARY KEY (id);


--
-- Name: medium_pkey; Type: CONSTRAINT; Schema: public; Owner: rmmusic; Tablespace: 
--

ALTER TABLE ONLY medium
    ADD CONSTRAINT medium_pkey PRIMARY KEY (id);


--
-- Name: recording_pkey; Type: CONSTRAINT; Schema: public; Owner: rmmusic; Tablespace: 
--

ALTER TABLE ONLY recording
    ADD CONSTRAINT recording_pkey PRIMARY KEY (id);


--
-- Name: song_pkey; Type: CONSTRAINT; Schema: public; Owner: rmmusic; Tablespace: 
--

ALTER TABLE ONLY song
    ADD CONSTRAINT song_pkey PRIMARY KEY (id);


--
-- Name: recording_medium_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: rmmusic
--

ALTER TABLE ONLY recording
    ADD CONSTRAINT recording_medium_id_fkey FOREIGN KEY (medium_id) REFERENCES medium(id);


--
-- Name: recording_song_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: rmmusic
--

ALTER TABLE ONLY recording
    ADD CONSTRAINT recording_song_id_fkey FOREIGN KEY (song_id) REFERENCES song(id);


--
-- Name: song_artist_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: rmmusic
--

ALTER TABLE ONLY song
    ADD CONSTRAINT song_artist_id_fkey FOREIGN KEY (artist_id) REFERENCES artist(id);


--
-- Name: public; Type: ACL; Schema: -; Owner: postgres
--

REVOKE ALL ON SCHEMA public FROM PUBLIC;
REVOKE ALL ON SCHEMA public FROM postgres;
GRANT ALL ON SCHEMA public TO postgres;
GRANT ALL ON SCHEMA public TO PUBLIC;


--
-- Name: artist; Type: ACL; Schema: public; Owner: rmmusic
--

REVOKE ALL ON TABLE artist FROM PUBLIC;
REVOKE ALL ON TABLE artist FROM rmmusic;
GRANT ALL ON TABLE artist TO rmmusic;
GRANT ALL ON TABLE artist TO postgres;


--
-- Name: artist_id_seq; Type: ACL; Schema: public; Owner: rmmusic
--

REVOKE ALL ON SEQUENCE artist_id_seq FROM PUBLIC;
REVOKE ALL ON SEQUENCE artist_id_seq FROM rmmusic;
GRANT ALL ON SEQUENCE artist_id_seq TO rmmusic;
GRANT ALL ON SEQUENCE artist_id_seq TO postgres;


--
-- Name: db_info; Type: ACL; Schema: public; Owner: rmmusic
--

REVOKE ALL ON TABLE db_info FROM PUBLIC;
REVOKE ALL ON TABLE db_info FROM rmmusic;
GRANT ALL ON TABLE db_info TO rmmusic;
GRANT ALL ON TABLE db_info TO postgres;


--
-- Name: medium; Type: ACL; Schema: public; Owner: rmmusic
--

REVOKE ALL ON TABLE medium FROM PUBLIC;
REVOKE ALL ON TABLE medium FROM rmmusic;
GRANT ALL ON TABLE medium TO rmmusic;
GRANT ALL ON TABLE medium TO postgres;


--
-- Name: medium_id_seq; Type: ACL; Schema: public; Owner: rmmusic
--

REVOKE ALL ON SEQUENCE medium_id_seq FROM PUBLIC;
REVOKE ALL ON SEQUENCE medium_id_seq FROM rmmusic;
GRANT ALL ON SEQUENCE medium_id_seq TO rmmusic;
GRANT ALL ON SEQUENCE medium_id_seq TO postgres;


--
-- Name: recording; Type: ACL; Schema: public; Owner: rmmusic
--

REVOKE ALL ON TABLE recording FROM PUBLIC;
REVOKE ALL ON TABLE recording FROM rmmusic;
GRANT ALL ON TABLE recording TO rmmusic;
GRANT ALL ON TABLE recording TO postgres;


--
-- Name: recording_id_seq; Type: ACL; Schema: public; Owner: rmmusic
--

REVOKE ALL ON SEQUENCE recording_id_seq FROM PUBLIC;
REVOKE ALL ON SEQUENCE recording_id_seq FROM rmmusic;
GRANT ALL ON SEQUENCE recording_id_seq TO rmmusic;
GRANT ALL ON SEQUENCE recording_id_seq TO postgres;


--
-- Name: song; Type: ACL; Schema: public; Owner: rmmusic
--

REVOKE ALL ON TABLE song FROM PUBLIC;
REVOKE ALL ON TABLE song FROM rmmusic;
GRANT ALL ON TABLE song TO rmmusic;
GRANT ALL ON TABLE song TO postgres;


--
-- Name: song_id_seq; Type: ACL; Schema: public; Owner: rmmusic
--

REVOKE ALL ON SEQUENCE song_id_seq FROM PUBLIC;
REVOKE ALL ON SEQUENCE song_id_seq FROM rmmusic;
GRANT ALL ON SEQUENCE song_id_seq TO rmmusic;
GRANT ALL ON SEQUENCE song_id_seq TO postgres;


--
-- Name: tag_medium; Type: ACL; Schema: public; Owner: rmmusic
--

REVOKE ALL ON TABLE tag_medium FROM PUBLIC;
REVOKE ALL ON TABLE tag_medium FROM rmmusic;
GRANT ALL ON TABLE tag_medium TO rmmusic;
GRANT ALL ON TABLE tag_medium TO postgres;


--
-- PostgreSQL database dump complete
--

