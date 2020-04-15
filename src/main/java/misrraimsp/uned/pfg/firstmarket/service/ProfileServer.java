package misrraimsp.uned.pfg.firstmarket.service;

import misrraimsp.uned.pfg.firstmarket.adt.dto.ProfileForm;
import misrraimsp.uned.pfg.firstmarket.converter.ProfileConverter;
import misrraimsp.uned.pfg.firstmarket.data.ProfileRepository;
import misrraimsp.uned.pfg.firstmarket.model.Profile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProfileServer {

    private ProfileRepository profileRepository;
    private ProfileConverter profileConverter;

    @Autowired
    public ProfileServer(ProfileRepository profileRepository, ProfileConverter profileConverter){

        this.profileRepository = profileRepository;
        this.profileConverter = profileConverter;
    }

    public Profile persist(Profile profile) {
        return profileRepository.save(profile);
    }

    public void edit(Long profileId, Profile newProfile) {
        newProfile.setId(profileId);
        profileRepository.save(newProfile);
    }

    public ProfileForm convertProfileToProfileForm(Profile profile) {
        return profileConverter.convertProfileToProfileForm(profile);
    }

    public Profile convertProfileFormToProfile(ProfileForm profileForm) {
        return profileConverter.convertProfileFormToProfile(profileForm);
    }
}
