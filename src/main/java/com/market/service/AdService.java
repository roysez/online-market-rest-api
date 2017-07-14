package com.market.service;

import com.market.entity.Ad;
import com.market.entity.InvalidAdStateTransitionException;
import com.market.repository.AdRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AdService {

    @Autowired
    private AdRepository adRepository;

    public Ad publish(Long id) throws InvalidAdStateTransitionException {
        return findDoAndSave(id, Ad::publish);
    }

    public Ad expire(Long id) throws InvalidAdStateTransitionException {
        return findDoAndSave(id, Ad::expire);
    }

    private Ad findDoAndSave(Long id, Action action) {
        Ad foundAd = adRepository.findOne(id);
        Ad modifiedAd = action.perform(foundAd);
        return adRepository.save(modifiedAd);
    }

    public Ad findOne(Long id) {
        return adRepository.findOne(id);
    }
    public void save(Ad ad){ adRepository.save(ad); }
    @FunctionalInterface
    private interface Action {

        Ad perform(Ad ad);

    }

}