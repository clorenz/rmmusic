package de.christophlorenz.rmmusic.web.controller;

import de.christophlorenz.rmmusic.model.Medium;
import de.christophlorenz.rmmusic.model.MediumTag;
import de.christophlorenz.rmmusic.model.TagMediumId;
import de.christophlorenz.rmmusic.persistence.jpa2.MediumRepository;
import de.christophlorenz.rmmusic.persistence.jpa2.MediumTagRepository;
import de.christophlorenz.rmmusic.web.model.Sticker;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by clorenz on 09.06.15.
 */
@Controller
@RequestMapping("/rmmusic/sticker")
public class StickerController {

    private static final Logger log = Logger.getLogger(StickerController.class);

    @Autowired
    MediumTagRepository mediumTagRepository;

    @Autowired
    MediumRepository mediumRepository;

    @RequestMapping("/print")
    public String printSticker(Model model) {
        List<MediumTag> mediumTags = mediumTagRepository.findByUserNameAndAction("clorenz", "sticker");
        List<Sticker> stickers = new ArrayList<Sticker>();

        for ( MediumTag mediumTag : mediumTags) {
            Sticker sticker = new Sticker();
            try {
                Medium m = mediumRepository.getOne(mediumTag.getId().getMediumId());
                sticker.setMediumCode(m.getMediumCode().replaceAll("\\s","&nbsp;"));
                sticker.setTitle(m.getTitle());
                if (m.getArtist() != null) {
                    String print = m.getArtist().getPrint();
                    sticker.setArtist(StringUtils.isBlank(print) ? m.getArtist().getName() : print);
                }
                sticker.setDate(m.getBuyDate());
                sticker.setFormattedPrice(String.format(Locale.GERMANY, "EUR %.2f", m.getBuyPrice()));
                stickers.add(sticker);
            } catch ( Exception e) {
                log.error("Cannot add sticker for ID="+mediumTag.getId().getMediumId()+": ",e);
            }
        }

        model.addAttribute("stickers", stickers);

        return "rmmusic/stickerList";
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public String createSticker(@Valid @ModelAttribute("medium") Medium medium,
                                Model model,
                                HttpServletRequest request,
                                RedirectAttributes redirectAttributes) {
        MediumTag mediumTag = new MediumTag();
        mediumTag.setAction("sticker");
        TagMediumId mediumTagId = new TagMediumId();
        mediumTagId.setUserName("clorenz");
        mediumTagId.setMediumId(medium.getId());
        mediumTag.setId(mediumTagId);
        mediumTagRepository.save(mediumTag);

        redirectAttributes.addFlashAttribute("success", "Added sticker for "+medium.getMediumCode());
        String redirect="/rmmusic/";
        String referer = request.getHeader("referer");
        String origin = request.getHeader("origin");
        redirect = referer.replace(origin, "");

        log.info("Redirecting to redirect:"+redirect);

        return "redirect:"+ redirect;
    }

    @RequestMapping("/delete")
    public String deleteSticker(Model model,
                                RedirectAttributes redirectAttributes) {
        List<MediumTag> oldMediumTags = mediumTagRepository.findByUserNameAndAction("clorenz", "sticker.backup");
        mediumTagRepository.delete(oldMediumTags);

        List<MediumTag> mediumTags = mediumTagRepository.findByUserNameAndAction("clorenz", "sticker");
        for ( MediumTag mediumTag: mediumTags ) {
            mediumTag.setAction("sticker.backup");
        }

        mediumTagRepository.save(mediumTags);

        redirectAttributes.addFlashAttribute("success", "Deleted stickers");
        return "redirect:/rmmusic/";
    }
}
