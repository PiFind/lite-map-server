package io.pifind.mapserver.controller;

import io.pifind.common.response.R;
import io.pifind.place.api.IAdministrativeAreaService;
import io.pifind.place.model.AdministrativeAreaDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/area")
public class AdministrativeAreaController {

    @Autowired
    private IAdministrativeAreaService administrativeAreaService;

    @GetMapping("/{id}/{layerLevel}")
    public R<AdministrativeAreaDTO> getAdministrativeAreaById(
            @PathVariable Long id,
            @PathVariable Integer layerLevel) {
        return administrativeAreaService.getAdministrativeAreaById(id,layerLevel);
    }

}
