package misrraimsp.uned.pfg.firstmarket.service;

import misrraimsp.uned.pfg.firstmarket.data.ProfileRepository;
import misrraimsp.uned.pfg.firstmarket.exception.EntityNotFoundByIdException;
import misrraimsp.uned.pfg.firstmarket.model.Profile;
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
