package de.christophlorenz.rmmusic.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;
import java.util.Date;

/**
 * Created by clorenz on 11.05.15.
 */
@Entity
@Table(name="MEDIUM", uniqueConstraints = @UniqueConstraint(columnNames = {"type","code"}))
@NamedQuery(name = "Medium.findLabelsIgnoreCaseStartingWithAsc", query = "select m from Medium m where lower(m.label) like lower(concat(?1,'%'))")
public class Medium {

    public static final int AUDIO_TAPE=0;
    public static final int VIDEO_TAPE=1;
    public static final int MD=2;
    public static final int ROM=3;
    public static final int FILES=4;
    public static final int LP=5;
    public static final int SINGLE=6;
    public static final int CD=7;


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private Integer type;

    @Column(length = 8)
    private String code;

    @ManyToOne(optional=true, targetEntity = Artist.class, fetch = FetchType.EAGER)
    @JoinColumn(name="artist_id", nullable=true)
    private Artist artist;

    private String title;
    private String label;
    private String ordercode;

    @Column(name="p_year")
    private Integer pYear;

    private Integer size;
    private String manufacturer;
    private String system;

    @Temporal(TemporalType.DATE)
    @Column(name="rec_begin_date")
    private Date recBeginDate;

    @Temporal(TemporalType.DATE)
    @Column(name="rec_begin_b")
    private Date recBeginB;

    @Temporal(TemporalType.DATE)
    @Column(name="rec_end_date")
    private Date recEndDate;

    @Temporal(TemporalType.DATE)
    @Column(name="burning_date")
    private Date burningDate;

    @Column(name="discid")
    private Long discId;

    @Column(name="track_offsets", length=255)
    private String trackOffsets;

    @Column(length=16)
    private String category;

    @Column(name="id3_genre", length=16)
    private String id3Genre;

    @Column(length=3)
    private String digital;

    private Boolean audio;
    private Boolean rewritable;
    private String magic;

    @Column(name="files_type")
    private String filesType;

    @Temporal(TemporalType.DATE)
    @Column(name="buy_date")
    private Date buyDate;

    @Column(name="buy_price")
    private Double buyPrice;

    private String remarks;

    @Temporal(TemporalType.TIMESTAMP)
    private Date timestamp;

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Artist getArtist() {
        return artist;
    }

    public void setArtist(Artist artist) {
        this.artist = artist;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getOrdercode() {
        return ordercode;
    }

    public void setOrdercode(String ordercode) {
        this.ordercode = ordercode;
    }

    public Integer getpYear() {
        return pYear;
    }

    public void setpYear(Integer pYear) {
        this.pYear = pYear;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getSystem() {
        return system;
    }

    public void setSystem(String system) {
        this.system = system;
    }

    public Date getRecBeginDate() {
        return recBeginDate;
    }

    public void setRecBeginDate(Date recBeginDate) {
        this.recBeginDate = recBeginDate;
    }

    public Date getRecBeginB() {
        return recBeginB;
    }

    public void setRecBeginB(Date recBeginB) {
        this.recBeginB = recBeginB;
    }

    public Date getRecEndDate() {
        return recEndDate;
    }

    public void setRecEndDate(Date recEndDate) {
        this.recEndDate = recEndDate;
    }

    public Date getBurningDate() {
        return burningDate;
    }

    public void setBurningDate(Date burningDate) {
        this.burningDate = burningDate;
    }

    public Long getDiscId() {
        return discId;
    }

    public void setDiscId(Long discId) {
        this.discId = discId;
    }

    public String getTrackOffsets() {
        return trackOffsets;
    }

    public void setTrackOffsets(String trackOffsets) {
        this.trackOffsets = trackOffsets;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getId3Genre() {
        return id3Genre;
    }

    public void setId3Genre(String id3Genre) {
        this.id3Genre = id3Genre;
    }

    public String getDigital() {
        return digital;
    }

    public void setDigital(String digital) {
        this.digital = digital;
    }

    public Boolean getAudio() {
        return audio;
    }

    public void setAudio(Boolean audio) {
        this.audio = audio;
    }

    public Boolean getRewritable() {
        return rewritable;
    }

    public void setRewritable(Boolean rewritable) {
        this.rewritable = rewritable;
    }

    public String getMagic() {
        return magic;
    }

    public void setMagic(String magic) {
        this.magic = magic;
    }

    public String getFilesType() {
        return filesType;
    }

    public void setFilesType(String filesType) {
        this.filesType = filesType;
    }

    public Date getBuyDate() {
        return buyDate;
    }

    public void setBuyDate(Date buyDate) {
        this.buyDate = buyDate;
    }

    public Double getBuyPrice() {
        return buyPrice;
    }

    public void setBuyPrice(Double buyPrice) {
        this.buyPrice = buyPrice;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}
