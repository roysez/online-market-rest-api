package com.market.domain.core.ad;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.PersistentEntityResource;
import org.springframework.data.rest.webmvc.PersistentEntityResourceAssembler;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@RepositoryRestController
public class AdController {

    @Autowired
    private AdRepository adRepository;

    @RequestMapping("/ads/{id}/publishing")
    @ResponseBody
    public PersistentEntityResource publish(@PathVariable("id") Long id,
                                            PersistentEntityResourceAssembler assembler) {
        Ad ad = adRepository.findOne(id);
        ad.publish();
        adRepository.save(ad);
        return assembler.toFullResource(ad);
    }

    @RequestMapping("/ads/{id}/expiration")
    @ResponseBody
    public PersistentEntityResource expire(@PathVariable("id") Long id,
                                           PersistentEntityResourceAssembler assembler) {
        Ad ad = adRepository.findOne(id);
        ad.expire();
        adRepository.save(ad);
        return assembler.toFullResource(ad);
    }



}
