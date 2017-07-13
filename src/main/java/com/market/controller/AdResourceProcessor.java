package com.market.controller;

import com.market.entity.Ad;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.support.RepositoryEntityLinks;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceProcessor;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@Component
public class AdResourceProcessor implements ResourceProcessor<Resource<Ad>> {

    @Autowired
    private RepositoryEntityLinks entityLinks;

    @Override
    public Resource<Ad> process(Resource<Ad> resource) {
        Ad ad = resource.getContent();

            Ad.Status status = ad.getStatus();
            if (status == Ad.Status.NEW) {
                resource.add(entityLinks.linkForSingleResource(Ad.class,
                        ad.getId()).withRel("update"));
                resource.add(entityLinks.linkForSingleResource(Ad.class,
                        ad.getId()).withRel("deletion"));
                resource.add(linkTo(methodOn(AdController.class).publish(ad.getId(),
                        null)).withRel("publishing"));
            }
            if (status == Ad.Status.PUBLISHED) {
                resource.add(linkTo(methodOn(AdController.class)
                        .expire(ad.getId(), null))
                        .withRel("expiration"));
            }

        return resource;
    }


}

