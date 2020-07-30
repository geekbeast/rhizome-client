package com.geekbeast.rhizome.jobs

import com.fasterxml.jackson.annotation.JsonTypeInfo
import java.util.concurrent.Callable

/**
 *
 * @author Matthew Tamayo-Rios &lt;matthew@openlattice.com&gt;
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
interface DistributableJob<R> : Callable<R?>