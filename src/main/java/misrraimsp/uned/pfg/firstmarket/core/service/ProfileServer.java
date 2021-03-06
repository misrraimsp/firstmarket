package misrraimsp.uned.pfg.firstmarket.core.service;

import misrraimsp.uned.pfg.firstmarket.core.data.ProfileRepository;
import misrraimsp.uned.pfg.firstmarket.core.model.Profile;
import misrraimsp.uned.pfg.firstmarket.util.exception.EntityNotFoundByIdException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProfileServer {

    private final ProfileRepository profileRepository;

    @Autowired
    public ProfileServer(ProfileRepository profileRepository){
        this.profileRepository = profileRepository;
    }

    public Profile persist(Profile profile) {
        return profileRepository.save(profile);
    }

    public Profile edit(Profile editedProfile) throws EntityNotFoundByIdException {
        if (profileRepository.existsById(editedProfile.getId())) {
            return profileRepository.save(editedProfile);
        }
        else {
            throw new EntityNotFoundByIdException(editedProfile.getId(),Profile.class.getSimpleName());
        }
    }
}
