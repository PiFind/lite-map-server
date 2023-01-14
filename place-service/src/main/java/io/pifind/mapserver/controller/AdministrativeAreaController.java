package io.pifind.mapserver.controller;

import io.pifind.common.response.R;
import io.pifind.place.api.IAdministrativeAreaService;
import io.pifind.place.model.AdministrativeAreaDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/area")
public class AdministrativeAreaController {

    @Autowired
    private IAdministrativeAreaService administrativeAreaService;

    @GetMapping("/get")
    public R<AdministrativeAreaDTO> getAdministrativeAreaById(
            @RequestParam("id") Long id,
            @RequestParam(value = "deep",defaultValue = "2",required = false) Integer deep
    ) {
        return administrativeAreaService.getAdministrativeAreaById(id,deep);
    }

    @GetMapping("/exist")
    public R<Boolean> existAdministrativeAreaById(
            @RequestParam("id") Long id
    ) {
        return administrativeAreaService.existAdministrativeAreaById(id);
    }

    @GetMapping("/detail")
    public R<String> getDetailedAddress(
            @RequestParam("id") Long id,
            @RequestParam(value = "separator",defaultValue = "/",required = false) String separator
    ) {
        return administrativeAreaService.getDetailedAddress(id,separator);
    }

}
