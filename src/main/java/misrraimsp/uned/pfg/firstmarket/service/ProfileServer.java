package misrraimsp.uned.pfg.firstmarket.service;

import misrraimsp.uned.pfg.firstmarket.data.ProfileRepository;
import misrraimsp.uned.pfg.firstmarket.model.Profile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProfileServer {

    private ProfileRepository profileRepository;

    @Autowired
    public ProfileServer(ProfileRepository profileRepository){
        this.profileRepository = profileRepository;
    }

    public Profile persist(Profile profile) {
        return profileRepository.save(profile);
    }

    public void edit(Long profileId, Profile newProfile) {
        newProfile.setId(profileId);
        profileRepository.save(newProfile);
    }
}
